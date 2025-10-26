package org.oldjopa.hls.model.user

import jakarta.persistence.*

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