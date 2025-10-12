package org.oldjopa.hls.api

import org.oldjopa.hls.dto.CreateUserDto
import org.oldjopa.hls.dto.UpdateUserDto
import org.oldjopa.hls.model.user.User
import org.springframework.http.ResponseEntity

interface UserApi {
    fun getAll(): List<User>
    fun get(id: Long): User
    fun create(newUser: CreateUserDto): ResponseEntity<Any>
    fun update(id: Long, dto: UpdateUserDto): User
    fun setRoles(id: Long, roles: Set<String>): User
    fun delete(id: Long)
}