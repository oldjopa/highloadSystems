package org.oldjopa.hls.service.user

import org.oldjopa.hls.common.exception.NotFoundException
import org.oldjopa.hls.model.user.Role
import org.oldjopa.hls.repository.user.RoleRepository
import org.springframework.stereotype.Service

@Service
class RoleService(
    private val roleRepository: RoleRepository
) {
    fun get(name: String): Role =
        roleRepository.findById(name).orElseThrow { NotFoundException("Role $name not found") }

    fun getAllByNames(names: Set<String>): List<Role> =
        names.map { get(it) }
}