package org.oldjopa.hls.api.controller

import org.oldjopa.hls.api.DealApi
import org.oldjopa.hls.dto.ChangeDealStatusRequest
import org.oldjopa.hls.dto.CreateDealRequest
import org.oldjopa.hls.dto.DealDto
import org.oldjopa.hls.service.DealService
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
class DealController(private val service: DealService) : DealApi {

    override fun getAll(@ParameterObject pageable: Pageable): ResponseEntity<Page<DealDto>> {
        val page = service.list(pageable)
        val headers = HttpHeaders().apply {
            add("X-Total-Count", page.totalElements.toString())
        }
        return ResponseEntity.ok().headers(headers).body(page)
    }

    override fun get(@PathVariable id: Long) = service.get(id)

    override fun history(@PathVariable id: Long) = service.history(id)

    override fun statuses() = service.listStatuses()

    override fun create(@RequestBody req: CreateDealRequest): ResponseEntity<Any> {
        val created = service.create(req)
        val headers = HttpHeaders()
        headers.add(HttpHeaders.LOCATION, "/api/deals/${created.id}")
        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(created.id)
    }

    override fun changeStatus(@PathVariable id: Long, @RequestBody req: ChangeDealStatusRequest) = service.changeStatus(id, req)
}