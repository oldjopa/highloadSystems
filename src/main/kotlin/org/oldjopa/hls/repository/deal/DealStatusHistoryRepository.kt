package org.oldjopa.hls.repository.deal

import org.oldjopa.hls.model.deal.DealStatusHistory
import org.springframework.data.jpa.repository.JpaRepository

interface DealStatusHistoryRepository : JpaRepository<DealStatusHistory, Long> {
    fun findByDealIdOrderByChangedAtAsc(dealId: Long): List<DealStatusHistory>
}
