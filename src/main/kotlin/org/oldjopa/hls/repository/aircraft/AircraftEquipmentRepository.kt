package org.oldjopa.hls.repository.aircraft

import org.oldjopa.hls.model.aircraft.AircraftEquipment
import org.springframework.data.jpa.repository.JpaRepository

interface AircraftEquipmentRepository : JpaRepository<AircraftEquipment, Long>