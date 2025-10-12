package org.oldjopa.hls.repository.aircraft

import org.oldjopa.hls.model.aircraft.Aircraft
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param



interface AircraftRepository : JpaRepository<Aircraft, Long> {
    fun existsBySerialNumber(serialNumber: String): Boolean

    // Explicit queries added to avoid Spring Data trying to derive a property named "Full"
    @EntityGraph(
        attributePaths = [
            "type",
            "type.engine",
            "techPassport",
        ]
    )
    @Query("select a from Aircraft a")
    fun findAllFull(): List<Aircraft>

    @EntityGraph(
        attributePaths = [
            "type",
            "type.engine",
            "techPassport",
        ]
    )
    @Query("select a from Aircraft a where a.id = :id")
    fun findByIdFull(@Param("id") id: Long): Aircraft?

    // Interface-based projection (nested)
    @EntityGraph(
        attributePaths = [
            "type",
            "type.engine",
            "techPassport",
        ]
    )
    fun findAllProjectedBy(): List<AircraftFullView>

    @EntityGraph(
        attributePaths = [
            "type",
            "type.engine",
            "techPassport",
            "techPassport.type",
            "techPassport.type.engine"
        ]
    )
    fun findProjectedById(id: Long): AircraftFullView?

    interface AircraftFullView {
        fun getId(): Long
        fun getSerialNumber(): String
        fun getRegistrationNumber(): String?
        fun getListedPrice(): java.math.BigDecimal?
        fun getCurrency(): String?
        fun getOwnerId(): Long
        fun getType(): AircraftTypeView
        fun getTechPassport(): TechPassportView?

        interface AircraftTypeView {
            fun getId(): Long
            fun getManufacturer(): String
            fun getModel(): String
            fun getVariant(): String?
            fun getEngineCount(): Int
            fun getEngine(): EngineView
            fun getMaxSeats(): Int
            fun getMaxTakeoffWeightKg(): Int?
            fun getRangeKm(): Int?
            fun getCruiseSpeedKnots(): Int?
            fun getPressurized(): Boolean
        }

        interface EngineView {
            fun getId(): Long
            fun getName(): String
            fun getType(): String   // enum -> String
            fun getPower(): Double
        }

        interface TechPassportView {
            fun getId(): Long
            fun getFlightHours(): Double
            fun getManufactureYear(): Int
            fun getEmptyWeightKg(): Int?
            fun getFuelCapacityL(): Int?
            fun getLengthM(): Double?
            fun getWingspanM(): Double?
            fun getHeightM(): Double?
            fun getNoiseCert(): String?
        }
    }
}
