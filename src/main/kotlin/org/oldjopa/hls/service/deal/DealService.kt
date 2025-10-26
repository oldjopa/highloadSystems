package org.oldjopa.hls.service.deal

import org.oldjopa.hls.common.exception.NotFoundException
import org.oldjopa.hls.common.exception.ValidationException
import org.oldjopa.hls.dto.ChangeDealStatusRequest
import org.oldjopa.hls.dto.CreateDealRequest
import org.oldjopa.hls.dto.DealDto
import org.oldjopa.hls.model.deal.Deal
import org.oldjopa.hls.model.deal.DealStatusHistory
import org.oldjopa.hls.repository.deal.DealRepository
import org.oldjopa.hls.service.aircraft.AircraftService
import org.oldjopa.hls.service.user.UserService
import org.oldjopa.hls.utls.toDto
import org.springframework.context.annotation.Lazy
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class DealService(
    private val dealRepository: DealRepository,
    private val dealStatusService: DealStatusService,
    private val historyService: DealStatusHistoryService,
    private val userService: UserService,
    @Lazy
    private val aircraftService: AircraftService
) {
    fun get(id: Long): DealDto = dealRepository.findById(id).orElseThrow {
        NotFoundException("Deal $id not found")
    }.toDto()

    fun list(pageable: Pageable): Page<DealDto> = dealRepository.findAll(pageable).map { it.toDto() }

    @Transactional
    fun create(req: CreateDealRequest): DealDto {
        if (dealRepository.existsByDealNumber(req.dealNumber)) {
            throw ValidationException("Deal number already exists: ${req.dealNumber}")
        }
        val aircraft = aircraftService.getEntity(req.aircraftId)
        val buyer = userService.get(req.buyerId)
        val seller = userService.get(req.sellerId)
        val status = dealStatusService.get(req.initialStatus)
        val deal = dealRepository.save(
            Deal(
                dealNumber = req.dealNumber,
                aircraft = aircraft,
                buyer = buyer,
                seller = seller,
                status = status
            )
        )
        historyService.save(
            DealStatusHistory(
                deal = deal,
                status = status,
                changedBy = buyer,
                comment = "Created"
            )
        )
        return deal.toDto()
    }

    @Transactional
    fun changeStatus(id: Long, req: ChangeDealStatusRequest): DealDto {
        val deal = dealRepository.findById(id).orElseThrow { NotFoundException("Deal $id not found") }
        val newStatus = dealStatusService.get(req.statusCode)
        if (deal.status.code == newStatus.code) return deal.toDto()
        if (deal.status.isTerminal) throw ValidationException("Deal already in terminal status: ${deal.status.code}")
        val changer = userService.get(req.changedBy)
        deal.status = newStatus
        if (newStatus.isTerminal) {
            deal.isActive = false
            deal.closedAt = Instant.now()
        }
        historyService.save(
            DealStatusHistory(
                deal = deal,
                status = newStatus,
                changedBy = changer,
                comment = req.comment
            )
        )
        return deal.toDto()
    }

    fun deleteAllByAircraftId(id: Long) {
        dealRepository.deleteAllByAircraftId(id)
    }
}