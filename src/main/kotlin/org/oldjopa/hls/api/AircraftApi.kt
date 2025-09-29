package org.oldjopa.hls.api

import jakarta.validation.Valid
import org.oldjopa.hls.dto.AircraftDto
import org.oldjopa.hls.dto.CreateAircraftEquipmentRequest
import org.oldjopa.hls.dto.CreateAircraftRequest
import org.oldjopa.hls.dto.UpdateAircraftRequest
import org.springframework.http.ResponseEntity

interface AircraftApi {
    fun getAll(): List<AircraftDto>
    fun get(id: Long): AircraftDto
    fun create(req: CreateAircraftRequest): ResponseEntity<Any>
    fun createAircraftType(@Valid req: CreateAircraftEquipmentRequest): ResponseEntity<Any>
    fun update(id: Long, req: UpdateAircraftRequest): AircraftDto
    fun delete(id: Long)
}