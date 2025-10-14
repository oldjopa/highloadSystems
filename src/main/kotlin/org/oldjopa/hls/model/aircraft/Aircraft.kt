package org.oldjopa.hls.model.aircraft

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.oldjopa.hls.model.feature.TechPassport
import org.oldjopa.hls.model.user.User
import java.math.BigDecimal

@Entity
@Table(
    name = "aircraft",
    uniqueConstraints = [
        UniqueConstraint(name = "uk_aircraft_serial", columnNames = ["serial_number"]),
        UniqueConstraint(name = "uk_aircraft_reg", columnNames = ["registration_number"])
    ]
)
class Aircraft(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    val type: AircraftEquipment,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "tech_passport_id")
    val techPassport: TechPassport?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    var owner: User,

    @Column(name = "serial_number", nullable = false)
    val serialNumber: String,

    @Column(name = "registration_number")
    var registrationNumber: String?,
    var listedPrice: BigDecimal?,
    var currency: String?
)