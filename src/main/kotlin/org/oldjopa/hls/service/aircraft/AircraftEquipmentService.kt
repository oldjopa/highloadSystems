package org.oldjopa.hls.service.aircraft

import org.oldjopa.hls.common.exception.NotFoundException
import org.oldjopa.hls.model.aircraft.AircraftEquipment
import org.oldjopa.hls.repository.aircraft.AircraftEquipmentRepository
import org.springframework.stereotype.Service

@Service
class AircraftEquipmentService(private val repo: AircraftEquipmentRepository) {
    fun get(id: Long) = repo.findById(id).orElseThrow { NotFoundException("Equipment $id not found") }
    fun save(equipment: AircraftEquipment) = repo.save(equipment)
}