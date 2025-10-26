package org.oldjopa.hls.service.deal

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.oldjopa.hls.common.exception.ValidationException
import org.oldjopa.hls.dto.CreateStatusRequest
import org.oldjopa.hls.model.deal.DealStatus
import org.oldjopa.hls.repository.deal.DealStatusRepository
import org.oldjopa.hls.service.deal.DealStatusService
import java.util.*

class DealStatusServiceTest {

    private val repo = mockk<DealStatusRepository>()
    private val service = DealStatusService(repo)

    @Test
    fun `get should return status when exists`() {
        val status = DealStatus("NEW", "New", "desc", 1, false)
        every { repo.findById("NEW") } returns Optional.of(status)

        val result = service.get("NEW")
        assertThat(result).isEqualTo(status)
    }

    @Test
    fun `get should throw NotFoundException when status does not exist`() {
        every { repo.findById("NEW") } returns Optional.empty()
        assertThrows<org.oldjopa.hls.common.exception.NotFoundException> {
            service.get("NEW")
        }
    }

    @Test
    fun `list should return sorted DTOs`() {
        val s1 = DealStatus("B", "B", "desc", 2, false)
        val s2 = DealStatus("A", "A", "desc", 1, false)
        every { repo.findAll() } returns listOf(s1, s2)

        val result = service.list()
        assertThat(result.map { it.code }).containsExactly("A", "B")
    }

    @Test
    fun `exists should return true if status exists`() {
        every { repo.existsById("NEW") } returns true
        assertThat(service.exists("NEW")).isTrue
    }

    @Test
    fun `exists should return false if status does not exist`() {
        every { repo.existsById("NEW") } returns false
        assertThat(service.exists("NEW")).isFalse
    }

    @Test
    fun `create should save and return status DTO`() {
        // Arrange
        val req = CreateStatusRequest(
            code = "NEW",
            name = "New",
            description = "desc",
            orderIndex = 1,
            isTerminal = false
        )
        val savedStatus = DealStatus(
            code = req.code,
            name = req.name,
            description = req.description,
            orderIndex = req.orderIndex,
            isTerminal = req.isTerminal
        )
        every { repo.existsById(req.code) } returns false
        every { repo.save(any<DealStatus>()) } returns savedStatus

        // Act
        val result = service.create(req)

        // Assert
        assertThat(result.code).isEqualTo(req.code)
        assertThat(result.name).isEqualTo(req.name)
        assertThat(result.description).isEqualTo(req.description)
        assertThat(result.orderIndex).isEqualTo(req.orderIndex)
        assertThat(result.isTerminal).isEqualTo(req.isTerminal)
        verify { repo.save(any<DealStatus>()) }
    }

    @Test
    fun `create should throws ValidateException `() {
        // Arrange
        val req = CreateStatusRequest(
            code = "NEW",
            name = "New",
            description = "desc",
            orderIndex = 1,
            isTerminal = false
        )

        every { repo.existsById(req.code) } returns true

        // Act & Assert
        val exception = assertThrows<ValidationException> {
            service.create(req)
        }
        assertThat(exception.message).isEqualTo("Status code already exists: ${req.code}")
    }
}
