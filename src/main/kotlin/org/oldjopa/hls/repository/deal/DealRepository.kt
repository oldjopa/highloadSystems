package org.oldjopa.hls.repository.deal

import org.oldjopa.hls.model.deal.Deal
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface DealRepository : JpaRepository<Deal, Long> {
    @Modifying
    @Query("""
        DELETE FROM deal
        where deal.aircraft_id = ?1
    """, nativeQuery = true)
    fun deleteAllByAircraftId(id: Long)

    fun existsByDealNumber(dealNumber: String): Boolean
}
