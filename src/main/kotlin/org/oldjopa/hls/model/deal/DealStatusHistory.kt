package org.oldjopa.hls.model.deal

import jakarta.persistence.*
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