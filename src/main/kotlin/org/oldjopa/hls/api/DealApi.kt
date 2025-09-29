package org.oldjopa.hls.api

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
    fun create(req: CreateDealRequest): ResponseEntity<Any>
    fun changeStatus(id: Long , req: ChangeDealStatusRequest): DealDto
}