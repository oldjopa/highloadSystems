package org.oldjopa.hls.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "role")
class Role(
    @Id
    @Column(length = 50)
    val name: String,

    val level: Int,
    @Column(columnDefinition = "text")
    val description: String? = ""
)