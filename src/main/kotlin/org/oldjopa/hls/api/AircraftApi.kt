package org.oldjopa.hls.api

import org.oldjopa.hls.dto.AircraftDto
import org.oldjopa.hls.dto.CreateAircraftRequest
import org.oldjopa.hls.dto.UpdateAircraftRequest
import org.springframework.http.ResponseEntity

interface AircraftApi {
    fun getAll(): List<AircraftDto>
    fun get(id: Long): AircraftDto
    fun create(req: CreateAircraftRequest): ResponseEntity<Any>
    fun update(id: Long, req: UpdateAircraftRequest): AircraftDto
    fun delete(id: Long)
}