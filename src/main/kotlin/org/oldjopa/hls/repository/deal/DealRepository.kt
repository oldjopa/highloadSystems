package org.oldjopa.hls.repository.deal

import org.oldjopa.hls.model.deal.Deal
import org.springframework.data.jpa.repository.JpaRepository

interface DealRepository : JpaRepository<Deal, Long> {
    fun existsByDealNumber(dealNumber: String): Boolean
}
