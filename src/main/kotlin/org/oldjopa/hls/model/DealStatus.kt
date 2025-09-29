package org.oldjopa.hls.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "deal_status")
class DealStatus(
    @Id
    @Column(length = 30)
    val code: String,
    val name: String,
    @Column(columnDefinition = "text")
    val description: String?,
    val orderIndex: Int,
    val isTerminal: Boolean
)