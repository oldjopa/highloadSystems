package org.oldjopa.hls.unit.aircraft

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.oldjopa.hls.common.exception.NotFoundException
import org.oldjopa.hls.model.feature.TechPassport
import org.oldjopa.hls.repository.feature.TechPassportRepository
import org.oldjopa.hls.service.aircraft.TechPassportService
import java.util.Optional

class TechPassportServiceTest {

    private val tpRepo = mockk<TechPassportRepository>()
    private val tpService = TechPassportService(tpRepo)

    @Test
    fun `get TechPassport should throw NotFoundException`() {
        // Arrange
        every { tpRepo.findById(1) } returns Optional.empty()

        // Act & Assert
        assertThrows<NotFoundException> { tpService.get(1) }
    }

    @Test
    fun `save TechPassport should persist entity`() {
        // Arrange
        val tp = mockk<TechPassport>()
        every { tpRepo.save(tp) } returns tp

        // Act
        val result = tpService.save(tp)

        // Assert
        assertThat(result).isEqualTo(tp)
        verify { tpRepo.save(tp) }
    }

    @Test
    fun `delete TechPassport should call repository deleteById`() {
        // Arrange
        every { tpRepo.deleteById(1) } returns Unit

        // Act
        tpService.delete(1)

        // Assert
        verify { tpRepo.deleteById(1) }
    }
}