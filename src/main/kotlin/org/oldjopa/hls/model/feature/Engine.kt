package org.oldjopa.hls.model.feature

import jakarta.persistence.*

enum class EngineType { PISTON, TURBOPROP, JET, ELECTRIC, OTHER }

@Entity
@Table(name = "engine")
class Engine(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String,
    @Enumerated(EnumType.STRING)
    val type: EngineType,
    val power: Double
)