package org.oldjopa.hls.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.math.BigDecimal

// Read DTO

data class AircraftDto(
    val id: Long,
    val typeId: Long,
    val techPassportId: Long?,
    val ownerId: Long,
    val serialNumber: String,
    val registrationNumber: String?,
    val listedPrice: BigDecimal?,
    val currency: String?
)

// Request DTOs

data class CreateAircraftRequest(
    @field:NotNull
    val typeId: Long,
    val techPassportId: Long?,
    @field:NotNull
    val ownerId: Long,
    @field:NotBlank @field:Size(max = 255)
    val serialNumber: String,
    @field:Size(max = 255)
    val registrationNumber: String?,
    val listedPrice: BigDecimal?,
    @field:Size(max = 3)
    val currency: String?
)

data class UpdateAircraftRequest(
    @field:Size(max = 255)
    val registrationNumber: String? = null,
    val ownerId: Long? = null,
    val listedPrice: BigDecimal? = null,
    @field:Size(max = 3)
    val currency: String? = null
)

