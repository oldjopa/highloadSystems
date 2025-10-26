package org.oldjopa.hls.model.feature

import jakarta.persistence.*

@Entity
@Table(name = "feature")
class Feature(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String
)