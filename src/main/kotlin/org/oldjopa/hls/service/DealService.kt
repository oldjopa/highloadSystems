package org.oldjopa.hls.service

import org.oldjopa.hls.common.exception.NotFoundException
import org.oldjopa.hls.common.exception.ValidationException
import org.oldjopa.hls.dto.*
import org.oldjopa.hls.model.deal.Deal
import org.oldjopa.hls.model.deal.DealStatus
import org.oldjopa.hls.model.deal.DealStatusHistory
import org.oldjopa.hls.repository.aircraft.AircraftRepository
import org.oldjopa.hls.repository.deal.DealRepository
import org.oldjopa.hls.repository.deal.DealStatusHistoryRepository
import org.oldjopa.hls.repository.deal.DealStatusRepository
import org.oldjopa.hls.repository.user.UserRepository
import org.oldjopa.hls.utls.toDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class DealService(
    private val dealRepository: DealRepository,
    private val dealStatusRepository: DealStatusRepository,
    private val historyRepository: DealStatusHistoryRepository,
    private val userRepository: UserRepository,
    private val aircraftRepository: AircraftRepository
) {
    fun get(id: Long): DealDto = dealRepository.findById(id).orElseThrow {
        NotFoundException("Deal $id not found")
    }.toDto()

    fun list(pageable: Pageable): Page<DealDto> = dealRepository.findAll(pageable).map { it.toDto() }

    fun listStatuses(): List<DealStatusDto> = dealStatusRepository.findAll()
        .sortedBy { it.orderIndex }
        .map { it.toDto() }

    fun history(dealId: Long): List<DealStatusHistoryDto> {
        ensureDealExists(dealId)
        return historyRepository.findByDealIdOrderByChangedAtAsc(dealId).map { it.toDto() }
    }

    @Transactional
    fun create(req: CreateDealRequest): DealDto {
        if (dealRepository.existsByDealNumber(req.dealNumber)) {
            throw ValidationException("Deal number already exists: ${req.dealNumber}")
        }
        val aircraft = aircraftRepository.findById(req.aircraftId).orElseThrow { NotFoundException("Aircraft ${req.aircraftId} not found") }
        val buyer = userRepository.findById(req.buyerId).orElseThrow { NotFoundException("User ${req.buyerId} not found") }
        val seller = userRepository.findById(req.sellerId).orElseThrow { NotFoundException("User ${req.sellerId} not found") }
        val status = dealStatusRepository.findById(req.initialStatus).orElseThrow { NotFoundException("Status ${req.initialStatus} not found") }
        val deal = dealRepository.save(
            Deal(
                dealNumber = req.dealNumber,
                aircraft = aircraft,
                buyer = buyer,
                seller = seller,
                status = status
            )
        )
        historyRepository.save(
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
        val newStatus = dealStatusRepository.findById(req.statusCode).orElseThrow { NotFoundException("Status ${req.statusCode} not found") }
        if (deal.status.code == newStatus.code) return deal.toDto()
        if (deal.status.isTerminal) throw ValidationException("Deal already in terminal status: ${deal.status.code}")
        val changer = userRepository.findById(req.changedBy).orElseThrow { NotFoundException("User ${req.changedBy} not found") }
        deal.status = newStatus
        if (newStatus.isTerminal) {
            deal.isActive = false
            deal.closedAt = Instant.now()
        }
        historyRepository.save(
            DealStatusHistory(
                deal = deal,
                status = newStatus,
                changedBy = changer,
                comment = req.comment
            )
        )
        return deal.toDto()
    }

    fun createStatus(req: org.oldjopa.hls.dto.CreateStatusRequest): DealStatusDto {
        if (dealStatusRepository.existsById(req.code)) {
            throw ValidationException("Status code already exists: ${req.code}")
        }
        val status = dealStatusRepository.save(
            DealStatus(
                code = req.code,
                name = req.name,
                description = req.description,
                orderIndex = req.orderIndex,
                isTerminal = req.isTerminal
            )
        )
        return status.toDto()
    }

    private fun ensureDealExists(id: Long) {
        if (!dealRepository.existsById(id)) throw NotFoundException("Deal $id not found")
    }
}