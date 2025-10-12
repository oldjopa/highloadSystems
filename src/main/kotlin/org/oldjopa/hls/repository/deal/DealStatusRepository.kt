package org.oldjopa.hls.repository.deal

import org.oldjopa.hls.model.deal.DealStatus
import org.springframework.data.jpa.repository.JpaRepository

interface DealStatusRepository : JpaRepository<DealStatus, String>
 {
}