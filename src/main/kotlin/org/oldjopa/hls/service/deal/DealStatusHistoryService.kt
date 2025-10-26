package org.oldjopa.hls.service.deal

import org.oldjopa.hls.dto.DealStatusHistoryDto
import org.oldjopa.hls.model.deal.DealStatusHistory
import org.oldjopa.hls.repository.deal.DealStatusHistoryRepository
import org.oldjopa.hls.utls.toDto
import org.springframework.stereotype.Service

@Service
class DealStatusHistoryService(
    private val historyRepository: DealStatusHistoryRepository
) {
    fun listByDealId(dealId: Long): List<DealStatusHistoryDto> =
        historyRepository.findByDealIdOrderByChangedAtAsc(dealId)
            .map { it.toDto() }

    fun save(history: DealStatusHistory): DealStatusHistory =
        historyRepository.save(history)
}