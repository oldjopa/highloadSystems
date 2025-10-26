package org.oldjopa.hls.model.deal

import jakarta.persistence.*
import org.oldjopa.hls.model.aircraft.Aircraft
import org.oldjopa.hls.model.user.User
import java.time.Instant

@Entity
@Table(name = "deal")
class Deal(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(unique = true)
    val dealNumber: String,
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "aircraft_id")
    val aircraft: Aircraft,
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "buyer_id")
    var buyer: User,
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "seller_id")
    var seller: User,
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "status_code")
    var status: DealStatus,
    var isActive: Boolean = true,
    var closedAt: Instant? = null,

    @Column(name = "created_at", nullable = false)
    var createdAt: Instant = Instant.EPOCH,
    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant = Instant.EPOCH
)