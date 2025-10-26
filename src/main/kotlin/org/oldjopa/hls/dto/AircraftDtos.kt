package org.oldjopa.hls.dto

import jakarta.validation.constraints.*
import org.oldjopa.hls.model.feature.EngineType
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

data class CreateEngineRequest(
    @field:NotNull @field:Size(max = 100)
    val name: String,
    @field:NotNull
    val type: EngineType,
    @field:Min(1) @field:Max(100000)
    val power: Double,
)

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
