package org.oldjopa.hls.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.*
import org.oldjopa.hls.model.feature.EngineType
import java.math.BigDecimal

// Read DTO
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class AircraftDto(
    @Schema(example = "1") val id: Long,
    @Schema(example = "10") val typeId: Long,
    @Schema(example = "5") val techPassportId: Long?,
    @Schema(example = "3") val ownerId: Long,
    @Schema(example = "SN-12345") val serialNumber: String,
    @Schema(example = "RA-77777") val registrationNumber: String?,
    @Schema(example = "1500000.00") val listedPrice: BigDecimal?,
    @Schema(example = "USD") val currency: String?
)

// Request DTOs
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
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

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class UpdateAircraftRequest(
    @field:Size(max = 255)
    val registrationNumber: String? = null,
    val ownerId: Long? = null,
    val listedPrice: BigDecimal? = null,
    @field:Size(max = 3)
    val currency: String? = null
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CreateAircraftEquipmentRequest(
    @field:NotBlank @field:Size(max = 100)
    val manufacturer: String,
    @field:NotBlank @field:Size(max = 100)
    val model: String,
    @field:Size(max = 100)
    val variant: String?,
    val description: String?,
    @field:Min(1) @field:Max(5)
    val engineCount: Int,
    @field:NotNull
    val engineId: Long,
    @field:Min(1) @field:Max(1000)
    val maxSeats: Int,
    @field:Max(100000)
    val maxTakeoffWeightKg: Int,
    @field:Max(20000)
    val rangeKm: Int,
    @field:Max(1000)
    val cruiseSpeedKnots: Int,
    @field:NotNull
    val pressurized: Boolean
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CreateEngineRequest(
    @field:NotNull @field:Size(max = 100)
    val name: String,
    @field:NotNull
    val type: EngineType,
    @field:Min(1) @field:Max(100000)
    val power: Double,
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CreateTechPassportRequest(
    @field:NotNull @field:Max(1000000)
    val flightHours: Double,
    @field:NotNull @field:Min(1930) @field:Max(2100)
    val manufactureYear: Int,
    val emptyWeightKg: Int?,
    val fuelCapacityL: Int?,
    val lengthM: Double?,
    val wingspanM: Double?,
    val heightM: Double?,
    val noiseCert: String?,
)
