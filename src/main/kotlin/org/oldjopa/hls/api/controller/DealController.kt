package org.oldjopa.hls.api.controller

import jakarta.validation.Valid
import org.oldjopa.hls.api.DealApi
import org.oldjopa.hls.service.DealService
import org.oldjopa.hls.dto.ChangeDealStatusRequest
import org.oldjopa.hls.dto.CreateDealRequest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/deals")
@Validated
class DealController(private val service: DealService) : DealApi {
    @GetMapping
    override fun getAll() = service.list()

    @GetMapping("/{id}")
    override fun get(@PathVariable id: Long) = service.get(id)

    @GetMapping("/{id}/history")
    override fun history(@PathVariable id: Long) = service.history(id)

    @GetMapping("/statuses")
    override fun statuses() = service.listStatuses()

    @PostMapping
    override fun create(@RequestBody req: CreateDealRequest): ResponseEntity<Any> {
        val created = service.create(req)
        val headers = HttpHeaders()
        headers.add(HttpHeaders.LOCATION, "/api/deals/${created.id}")
        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(created)
    }

    @PostMapping("/{id}/status")
    override fun changeStatus(@PathVariable id: Long, @RequestBody req: ChangeDealStatusRequest) = service.changeStatus(id, req)
}