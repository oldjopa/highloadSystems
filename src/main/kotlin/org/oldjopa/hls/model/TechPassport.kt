package org.oldjopa.hls.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "tech_passport")
class TechPassport(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val flightHours: Double,
    val manufactureYear: Int,
    val emptyWeightKg: Int?,

    //setting column name cuz it translates poorly to snake case
    @Column(name = "fuel_capacity_l")
    val fuelCapacityL: Int?,
    @Column(name = "length_m")
    val lengthM: Double?,
    @Column(name = "wingspan_m")
    val wingspanM: Double?,
    @Column(name = "height_m")
    val heightM: Double?,
    val noiseCert: String?,
    @ManyToMany
    @JoinTable(
        name = "tech_passport_feature",
        joinColumns = [JoinColumn(name = "tech_passport_id")],
        inverseJoinColumns = [JoinColumn(name = "feature_id")]
    )
    val features: MutableSet<Feature> = mutableSetOf(),
    val createdAt: Instant,
    val updatedAt: Instant,
)