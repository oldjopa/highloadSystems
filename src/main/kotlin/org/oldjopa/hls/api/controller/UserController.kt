package org.oldjopa.hls.api.controller

import jakarta.validation.Valid
import org.oldjopa.hls.api.UserApi
import org.oldjopa.hls.dto.CreateUserDto
import org.oldjopa.hls.dto.UpdateUserDto
import org.oldjopa.hls.model.user.User
import org.oldjopa.hls.service.user.UserService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.data.domain.Pageable
import org.springdoc.core.annotations.ParameterObject

@RestController
class UserController(private val service: UserService) : UserApi {
    override fun getAll(@ParameterObject pageable: Pageable) = service.list(pageable)

    override fun get(@PathVariable id: Long): User = service.get(id)

    override fun create(@RequestBody @Valid newUser: CreateUserDto): ResponseEntity<Any> {
        val created = service.create(newUser)
        val headers = HttpHeaders()
        headers.add(HttpHeaders.LOCATION, "/api/users/${created.id}")
        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(created)
    }

    override fun update(@PathVariable id: Long, @RequestBody @Valid dto: UpdateUserDto) = service.update(id, dto)

    override fun setRoles(@PathVariable id: Long, @RequestBody roles: Set<String>) = service.assignRoles(id, roles)

    @ResponseStatus(HttpStatus.NO_CONTENT)
    override fun delete(@PathVariable id: Long) = service.delete(id)
}