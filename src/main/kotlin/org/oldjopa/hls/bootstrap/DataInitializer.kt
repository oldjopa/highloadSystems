package org.oldjopa.hls.bootstrap

import org.oldjopa.hls.model.user.Role
import org.oldjopa.hls.model.user.User
import org.oldjopa.hls.repository.deal.DealStatusRepository
import org.oldjopa.hls.repository.user.RoleRepository
import org.oldjopa.hls.repository.user.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/**
 * Simple data seeding for local/dev profile. Liquibase seeds the statuses; here we add roles & sample users if missing.
 */
@Component
@Profile("dev", "local")
class DataInitializer(
    private val roleRepository: RoleRepository,
    private val userRepository: UserRepository,
    private val dealStatusRepository: DealStatusRepository
) : ApplicationRunner {
    private val log = LoggerFactory.getLogger(DataInitializer::class.java)

    @Transactional
    override fun run(args: ApplicationArguments) {
        seedRoles()
        seedUsers()
        log.info("DataInitializer completed. Users=${userRepository.count()} Roles=${roleRepository.count()} Statuses=${dealStatusRepository.count()}")
    }

    private fun seedRoles() {
        if (roleRepository.count() > 0) return
        roleRepository.saveAll(
            listOf(
                Role(name = "ADMIN", level = 100, description = "Administrator"),
                Role(name = "BROKER", level = 50, description = "Broker user"),
                Role(name = "USER", level = 10, description = "Regular user")
            )
        )
        log.info("Seeded roles")
    }

    private fun seedUsers() {
        if (userRepository.count() > 0) return
        val adminRole = roleRepository.findById("ADMIN").get()
        val userRole = roleRepository.findById("USER").get()
        userRepository.saveAll(
            listOf(
                User(
                    email = "admin@example.com",
                    hashedPassword = "{noop}admin", // placeholder; use encoder in real app
                    firstName = "Admin",
                    lastName = "User",
                    roles = mutableSetOf(adminRole, userRole)
                ),
                User(
                    email = "buyer@example.com",
                    hashedPassword = "{noop}buyer",
                    firstName = "Buyer",
                    lastName = "User",
                    roles = mutableSetOf(userRole)
                ),
                User(
                    email = "seller@example.com",
                    hashedPassword = "{noop}seller",
                    firstName = "Seller",
                    lastName = "User",
                    roles = mutableSetOf(userRole)
                )
            )
        )
        log.info("Seeded sample users")
    }
}

