package org.oldjopa.hls.service.deal

import org.oldjopa.hls.common.exception.NotFoundException
import org.oldjopa.hls.common.exception.ValidationException
import org.oldjopa.hls.dto.CreateStatusRequest
import org.oldjopa.hls.dto.DealStatusDto
import org.oldjopa.hls.model.deal.DealStatus
import org.oldjopa.hls.repository.deal.DealStatusRepository
import org.oldjopa.hls.utls.toDto
import org.springframework.stereotype.Service

@Service
class DealStatusService(
    private val dealStatusRepository: DealStatusRepository
) {
    fun get(code: String): DealStatus =
        dealStatusRepository.findById(code)
            .orElseThrow { NotFoundException("Status $code not found") }

    fun list(): List<DealStatusDto> =
        dealStatusRepository.findAll()
            .sortedBy { it.orderIndex }
            .map { it.toDto() }

    fun exists(code: String): Boolean = dealStatusRepository.existsById(code)
}