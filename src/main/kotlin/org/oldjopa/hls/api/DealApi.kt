package org.oldjopa.hls.api

import jakarta.validation.Valid
import org.oldjopa.hls.dto.ChangeDealStatusRequest
import org.oldjopa.hls.dto.CreateDealRequest
import org.oldjopa.hls.dto.DealDto
import org.oldjopa.hls.dto.DealStatusDto
import org.oldjopa.hls.dto.DealStatusHistoryDto
import org.springframework.http.ResponseEntity

interface DealApi {
    fun getAll(): List<DealDto>
    fun get(id: Long): DealDto
    fun history(id: Long):  List<DealStatusHistoryDto>
    fun statuses(): List<DealStatusDto>
    fun create(@Valid req: CreateDealRequest): ResponseEntity<Any>
    fun changeStatus(id: Long, @Valid req: ChangeDealStatusRequest): DealDto
}