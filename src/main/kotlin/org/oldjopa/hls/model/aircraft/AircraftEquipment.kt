package org.oldjopa.hls.model.aircraft

import jakarta.persistence.*
import org.oldjopa.hls.model.feature.Engine

@Entity
@Table(name = "aircraft_equipment")
class AircraftEquipment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val manufacturer: String,
    val model: String,
    val variant: String?,

    @Column(columnDefinition = "text")
    val description: String?,

    val engineCount: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "engine_id")
    val engine: Engine,

    val maxSeats: Int,
    val maxTakeoffWeightKg: Int,
    val rangeKm: Int,
    val cruiseSpeedKnots: Int,
    val pressurized: Boolean
)