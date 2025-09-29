package org.oldjopa.hls.api.controller

import jakarta.validation.Valid
import org.oldjopa.hls.api.AircraftApi
import org.oldjopa.hls.dto.CreateAircraftRequest
import org.oldjopa.hls.dto.UpdateAircraftRequest
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

@RestController
@RequestMapping("/api/aircraft")
@Validated
class AircraftController(private val service: AircraftService) : AircraftApi {
    @GetMapping
    override fun getAll() = service.list()

    @GetMapping("/{id}")
    override fun get(@PathVariable id: Long) = service.get(id)

    @PostMapping
    override fun create(@RequestBody @Valid req: CreateAircraftRequest): ResponseEntity<Any> {
        val created = service.create(req)
        val headers = HttpHeaders().apply { add(HttpHeaders.LOCATION, "/api/aircraft/${created.id}") }
        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(created)
    }

    @PatchMapping("/{id}")
    override fun update(@PathVariable id: Long, @RequestBody @Valid req: UpdateAircraftRequest) = service.update(id, req)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    override fun delete(@PathVariable id: Long) = service.delete(id)
}