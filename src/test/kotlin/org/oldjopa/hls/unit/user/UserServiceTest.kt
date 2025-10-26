package org.oldjopa.hls.unit.user

import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.oldjopa.hls.dto.CreateUserDto
import org.oldjopa.hls.dto.UpdateUserDto
import org.oldjopa.hls.model.user.Role
import org.oldjopa.hls.model.user.User
import org.oldjopa.hls.repository.user.RoleRepository
import org.oldjopa.hls.repository.user.UserRepository
import org.oldjopa.hls.service.UserService
import org.springframework.data.domain.Pageable
import java.util.*

class UserServiceTest {

    private val userRepository = mockk<UserRepository>()
    private val roleRepository = mockk<RoleRepository>()
    private val service = UserService(userRepository, roleRepository)

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    private fun role(name: String) = Role(name, 1, "desc")
    private fun user(id: Long) = User(
        id = id,
        email = "user$id@example.com",
        hashedPassword = "pwd",
        firstName = "F$id",
        lastName = "L$id",
        roles = mutableSetOf()
    )

    @Test
    fun `create user success`() {
        // Arrange
        val dto = CreateUserDto(
            email = "new@example.com",
            phone = "123",
            firstName = "John",
            lastName = "Doe",
            roles = setOf("USER")
        )
        every { roleRepository.findAllById(setOf("USER")) } returns listOf(role("USER"))
        val slot = slot<User>()
        every { userRepository.save(capture(slot)) } answers {
            val u = slot.captured
            User(
                id = 1,
                email = u.email,
                phone = u.phone,
                hashedPassword = u.hashedPassword,
                firstName = u.firstName,
                lastName = u.lastName,
                roles = u.roles
            )
        }

        // Act
        val u = service.create(dto)

        // Assert
        assertEquals(1, u.id)
        assertEquals("John", u.firstName)
        assertEquals(1, u.roles.size)
        verify { userRepository.save(any()) }
    }

    @Test
    fun `update user fields`() {
        // Arrange
        val u = user(1)
        every { userRepository.findById(1) } returns Optional.of(u)
        val dto = UpdateUserDto(firstName = "Jane", lastName = "Smith")

        // Act
        val updated = service.update(1, dto)

        // Assert
        assertEquals("Jane", updated.firstName)
        assertEquals("Smith", updated.lastName)
        assertEquals("newpwd", updated.hashedPassword)
    }

    @Test
    fun `assign roles replaces old roles`() {
        // Arrange
        val u = user(1)
        u.roles.add(role("OLD"))
        every { userRepository.findById(1) } returns Optional.of(u)
        every { roleRepository.findAllById(setOf("NEW")) } returns listOf(role("NEW"))

        // Act
        val updated = service.assignRoles(1, setOf("NEW"))

        // Assert
        assertEquals(1, updated.roles.size)
        assertTrue(updated.roles.any { it.name == "NEW" })
    }

    @Test
    fun `delete calls repository`() {
        // Arrange
        every { userRepository.deleteById(1) } just Runs

        // Act
        service.delete(1)

        // Assert
        verify { userRepository.deleteById(1) }
    }

    @Test
    fun `get returns user`() {
        // Arrange
        val u = user(1)
        every { userRepository.findById(1) } returns Optional.of(u)

        // Act
        val result = service.get(1)

        // Assert
        assertEquals(1, result.id)
    }

    @Test
    fun `list returns paged users`() {
        // Arrange
        val users = listOf(user(1), user(2))
        val pageable = mockk<Pageable>()
        val page = org.springframework.data.domain.PageImpl(users)
        every { userRepository.findAll(pageable) } returns page

        // Act
        val result = service.list(pageable)

        // Assert
        assertEquals(2, result.content.size)
        assertEquals(user(1).id, result.content[0].id)
        assertEquals(user(2).id, result.content[1].id)
        verify { userRepository.findAll(pageable) }
    }
}
