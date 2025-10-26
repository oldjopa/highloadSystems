package org.oldjopa.hls.unit.user

import org.oldjopa.hls.service.user.RoleService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.oldjopa.hls.common.exception.NotFoundException
import org.oldjopa.hls.model.user.Role
import org.oldjopa.hls.repository.user.RoleRepository
import java.util.*

class RoleServiceTest {

    private val roleRepository: RoleRepository = mockk()
    private val roleService = RoleService(roleRepository)

    @Test
    fun `get returns role when exists`() {
        val role = Role("ADMIN", 1, "Admin role")
        every { roleRepository.findById("ADMIN") } returns Optional.of(role)

        val result = roleService.get("ADMIN")

        assertEquals(role, result)
        verify(exactly = 1) { roleRepository.findById("ADMIN") }
    }

    @Test
    fun `get throws NotFoundException when role does not exist`() {
        every { roleRepository.findById("USER") } returns Optional.empty()

        val ex = assertThrows<NotFoundException> { roleService.get("USER") }
        assertEquals("Role USER not found", ex.message)
        verify(exactly = 1) { roleRepository.findById("USER") }
    }

    @Test
    fun `getAllByNames returns list of roles`() {
        val admin = Role("ADMIN", 1, "Admin role")
        val user = Role("USER", 2, "User role")
        every { roleRepository.findById("ADMIN") } returns Optional.of(admin)
        every { roleRepository.findById("USER") } returns Optional.of(user)

        val result = roleService.getAllByNames(setOf("ADMIN", "USER"))

        assertEquals(listOf(admin, user), result)
        verify(exactly = 1) { roleRepository.findById("ADMIN") }
        verify(exactly = 1) { roleRepository.findById("USER") }
    }

    @Test
    fun `getAllByNames throws NotFoundException if one role missing`() {
        val admin = Role("ADMIN", 1, "Admin role")
        every { roleRepository.findById("ADMIN") } returns Optional.of(admin)
        every { roleRepository.findById("USER") } returns Optional.empty()

        val ex = assertThrows<NotFoundException> { roleService.getAllByNames(setOf("ADMIN", "USER")) }
        assertEquals("Role USER not found", ex.message)
    }
}
