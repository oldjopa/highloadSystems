package org.oldjopa.hls.api

import jakarta.validation.Valid
import org.oldjopa.hls.dto.AircraftDto
import org.oldjopa.hls.dto.CreateAircraftEquipmentRequest
import org.oldjopa.hls.dto.CreateAircraftRequest
import org.oldjopa.hls.dto.CreateEngineRequest
import org.oldjopa.hls.dto.CreateTechPassportRequest
import org.oldjopa.hls.dto.UpdateAircraftRequest
import org.springframework.http.ResponseEntity

interface AircraftApi {
    fun getAll(): List<AircraftDto>
    fun get(id: Long): AircraftDto
    fun getFull(id: Long): Any
    fun getAllFull(): List<Any>
    fun create(@Valid req: CreateAircraftRequest): ResponseEntity<Any>
    fun createAircraftType(@Valid req: CreateAircraftEquipmentRequest): ResponseEntity<Any>
    fun createEngine(@Valid req: CreateEngineRequest): ResponseEntity<Any>
    fun createTechPassport(@Valid req: CreateTechPassportRequest): ResponseEntity<Any>
    fun update(id: Long, @Valid req: UpdateAircraftRequest): AircraftDto
    fun delete(id: Long)
}