package org.oldjopa.hls.api.controller

import jakarta.validation.Valid
import org.oldjopa.hls.api.UserApi
import org.oldjopa.hls.dto.CreateUserDto
import org.oldjopa.hls.dto.UpdateUserDto
import org.oldjopa.hls.model.User
import org.oldjopa.hls.service.UserService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(private val service: UserService) : UserApi {
    @GetMapping
    override fun getAll() = service.list()

    @GetMapping("/{id}")
    override fun get(@PathVariable id: Long): User = service.get(id)

    @PostMapping
    override fun create(@RequestBody @Valid newUser: CreateUserDto): ResponseEntity<Any> {
        val created = service.create(newUser)
        val headers = HttpHeaders()
        headers.add(HttpHeaders.LOCATION, "/api/users/${created.id}")
        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(created)
    }

    @PatchMapping("/{id}")
    override fun update(@PathVariable id: Long, @RequestBody @Valid dto: UpdateUserDto) = service.update(id, dto)

    @PutMapping("/{id}/roles")
    override fun setRoles(@PathVariable id: Long, @RequestBody roles: Set<String>) = service.assignRoles(id, roles)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    override fun delete(@PathVariable id: Long) = service.delete(id)
}