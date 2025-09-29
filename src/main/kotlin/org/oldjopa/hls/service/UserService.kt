package org.oldjopa.hls.service

import org.oldjopa.hls.dto.CreateUserDto
import org.oldjopa.hls.dto.UpdateUserDto
import org.oldjopa.hls.model.User
import org.oldjopa.hls.repository.RoleRepository
import org.oldjopa.hls.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository
) {
    fun get(id: Long) = userRepository.findById(id).orElseThrow()
    fun list() = userRepository.findAll()

    @Transactional
    fun create(newUser: CreateUserDto): User {
        val roles = roleRepository.findAllById(newUser.roles).toMutableSet()
        return userRepository.save(
            User(
                email = newUser.email,
                phone = newUser.phone,
                hashedPassword = newUser.hashedPassword,
                firstName = newUser.firstName,
                lastName = newUser.lastName,
                roles = roles
            )
        )
    }

    @Transactional
    fun update(id: Long, newData: UpdateUserDto): User {
        val u = get(id)
        newData.firstName?.let { u.firstName = it }
        newData.lastName?.let { u.lastName = it }
        newData.hashedPassword?.let { u.hashedPassword = it }
        return u
    }

    @Transactional
    fun assignRoles(id: Long, roleNames: Set<String>): User {
        val u = get(id)
        val roles = roleRepository.findAllById(roleNames)
        u.roles.clear()
        u.roles.addAll(roles)
        return u
    }

    @Transactional
    fun delete(id: Long) = userRepository.deleteById(id)
}