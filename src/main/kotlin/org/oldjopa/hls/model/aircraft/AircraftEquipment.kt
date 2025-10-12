package org.oldjopa.hls.model.aircraft

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
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