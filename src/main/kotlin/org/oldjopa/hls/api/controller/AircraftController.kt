package org.oldjopa.hls.api.controller

import org.oldjopa.hls.api.AircraftApi
import org.oldjopa.hls.dto.CreateAircraftRequest
import org.oldjopa.hls.dto.UpdateAircraftRequest
import org.oldjopa.hls.dto.CreateAircraftEquipmentRequest
import org.oldjopa.hls.dto.CreateEngineRequest
import org.oldjopa.hls.dto.CreateTechPassportRequest
import org.oldjopa.hls.service.AircraftService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.data.domain.Pageable
import org.springdoc.core.annotations.ParameterObject

@RestController
@Validated
class AircraftController(private val service: AircraftService) : AircraftApi {

    override fun getAll(@ParameterObject pageable: Pageable) = service.list(pageable)

    override fun getAllFull() = service.listFull()

    override fun getFull(@PathVariable id: Long) = service.getFull(id)

    override fun get(@PathVariable id: Long) = service.get(id)

    override fun create(@RequestBody req: CreateAircraftRequest): ResponseEntity<Any> {
        val created = service.create(req)
        val headers = HttpHeaders().apply { add(HttpHeaders.LOCATION, "/api/aircraft/${created.id}") }
        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(created.id)
    }

    override fun createAircraftType(@RequestBody req: CreateAircraftEquipmentRequest): ResponseEntity<Any> {
        val createdId = service.createAircraftType(req)
        val headers = HttpHeaders().apply { add(HttpHeaders.LOCATION, "/api/aircraft/equipment/$createdId") }
        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(mapOf("id" to createdId))
    }

    override fun createEngine(@RequestBody req: CreateEngineRequest): ResponseEntity<Any> {
        val createdId = service.createEngine(req)
        val headers = HttpHeaders().apply { add(HttpHeaders.LOCATION, "/api/aircraft/engine/$createdId") }
        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(mapOf("id" to createdId))
    }

    override fun createTechPassport(@RequestBody req: CreateTechPassportRequest): ResponseEntity<Any> {
        val createdId = service.createTechPassport(req)
        val headers = HttpHeaders().apply { add(HttpHeaders.LOCATION, "/api/aircraft/tech-passport/$createdId") }
        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(mapOf("id" to createdId))
    }

    override fun update(@PathVariable id: Long, @RequestBody req: UpdateAircraftRequest) =
        service.update(id, req)

    @ResponseStatus(HttpStatus.NO_CONTENT)
    override fun delete(@PathVariable id: Long) = service.delete(id)
}