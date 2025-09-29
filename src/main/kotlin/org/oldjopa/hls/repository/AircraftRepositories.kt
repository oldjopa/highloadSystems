package org.oldjopa.hls.repository

import org.oldjopa.hls.model.Aircraft
import org.oldjopa.hls.model.AircraftEquipment
import org.oldjopa.hls.model.Engine
import org.oldjopa.hls.model.Feature
import org.oldjopa.hls.model.TechPassport
import org.springframework.data.jpa.repository.JpaRepository

interface EngineRepository : JpaRepository<Engine, Long>
interface AircraftEquipmentRepository : JpaRepository<AircraftEquipment, Long>
interface FeatureRepository : JpaRepository<Feature, Long>
interface TechPassportRepository : JpaRepository<TechPassport, Long>
interface AircraftRepository : JpaRepository<Aircraft, Long> {
    fun existsBySerialNumber(serialNumber: String): Boolean
}
