package org.oldjopa.hls.repository.feature

import org.oldjopa.hls.model.feature.TechPassport
import org.springframework.data.jpa.repository.JpaRepository

interface TechPassportRepository : JpaRepository<TechPassport, Long>
 {
}