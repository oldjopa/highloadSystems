package org.oldjopa.hls.repository.feature

import org.oldjopa.hls.model.feature.Engine
import org.springframework.data.jpa.repository.JpaRepository

interface EngineRepository : JpaRepository<Engine, Long> {
}