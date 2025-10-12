package org.oldjopa.hls.model.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
    name = "app_user",
    uniqueConstraints = [
        UniqueConstraint(name = "uk_user_email", columnNames = ["email"]),
        UniqueConstraint(name = "uk_user_phone", columnNames = ["phone"])
    ]
)
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(length = 320, nullable = false)
    val email: String,
    @Column(length = 32)
    val phone: String? = null,
    @Column(nullable = false)
    var hashedPassword: String,
    @Column(length = 100)
    var firstName: String,
    @Column(length = 100)
    var lastName: String,
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_role",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_name")]
    )
    val roles: MutableSet<Role> = mutableSetOf()
)