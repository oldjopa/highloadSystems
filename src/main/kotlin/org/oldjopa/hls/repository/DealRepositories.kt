package org.oldjopa.hls.repository

import org.oldjopa.hls.model.Deal
import org.oldjopa.hls.model.DealStatus
import org.oldjopa.hls.model.DealStatusHistory
import org.springframework.data.jpa.repository.JpaRepository

interface DealRepository : JpaRepository<Deal, Long> {
    fun existsByDealNumber(dealNumber: String): Boolean
}
interface DealStatusRepository : JpaRepository<DealStatus, String>
interface DealStatusHistoryRepository : JpaRepository<DealStatusHistory, Long> {
    fun findByDealIdOrderByChangedAtAsc(dealId: Long): List<DealStatusHistory>
}
