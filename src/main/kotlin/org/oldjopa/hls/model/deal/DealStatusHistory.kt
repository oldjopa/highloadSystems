package org.oldjopa.hls.model.deal

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.oldjopa.hls.model.user.User
import java.time.Instant

@Entity
@Table(name = "deal_status_history")
class DealStatusHistory(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "deal_id")
    val deal: Deal,
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "status_code")
    val status: DealStatus,
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "changed_by")
    val changedBy: User,
    val changedAt: Instant = Instant.now(),
    @Column(columnDefinition = "text")
    val comment: String?
)