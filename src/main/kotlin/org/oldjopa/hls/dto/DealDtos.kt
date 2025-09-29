package org.oldjopa.hls.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.Instant

// Read DTOs

data class DealStatusDto(
    val code: String,
    val name: String,
    val description: String?,
    val orderIndex: Int,
    val isTerminal: Boolean
)

data class DealDto(
    val id: Long,
    val dealNumber: String,
    val aircraftId: Long,
    val buyerId: Long,
    val sellerId: Long,
    val status: DealStatusDto,
    val isActive: Boolean,
    val closedAt: Instant?
)

data class DealStatusHistoryDto(
    val id: Long,
    val status: DealStatusDto,
    val changedByUserId: Long,
    val changedAt: Instant,
    val comment: String?
)

// Request DTOs

data class CreateDealRequest(
    @field:NotBlank @field:Size(max = 255)
    val dealNumber: String,
    @field:NotNull
    val aircraftId: Long,
    @field:NotNull
    val buyerId: Long,
    @field:NotNull
    val sellerId: Long,
    @field:NotBlank
    val initialStatus: String
)

data class ChangeDealStatusRequest(
    @field:NotBlank
    val statusCode: String,
    @field:NotNull
    val changedBy: Long,
    val comment: String?
)

