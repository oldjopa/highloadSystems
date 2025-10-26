package org.oldjopa.hls.unit.aircraft

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.oldjopa.hls.common.exception.NotFoundException
import org.oldjopa.hls.model.aircraft.AircraftEquipment
import org.oldjopa.hls.repository.aircraft.AircraftEquipmentRepository
import org.oldjopa.hls.service.aircraft.AircraftEquipmentService
import java.util.Optional

class AircraftEquipmentServiceTest {
    private val equipmentRepo = mockk<AircraftEquipmentRepository>()
    private val equipmentService = AircraftEquipmentService(equipmentRepo)

    @Test
    fun `get should return equipment`() {
        // Arrange
        val equipment = AircraftEquipment(
            manufacturer = "M", model = "X", variant = null, description = null,
            engineCount = 2, engine = mockk(), maxSeats = 4, maxTakeoffWeightKg = 1000,
            rangeKm = 500, cruiseSpeedKnots = 300, pressurized = true
        )
        every { equipmentRepo.findById(1) } returns Optional.of(equipment)

        // Act
        val result = equipmentService.get(1)

        // Assert
        assertThat(result).isEqualTo(equipment)
    }

    @Test
    fun `get should throw NotFoundException`() {
        // Arrange
        every { equipmentRepo.findById(1) } returns Optional.empty()

        // Act & Assert
        assertThrows<NotFoundException> { equipmentService.get(1) }
    }

    @Test
    fun `save should persist equipment`() {
        // Arrange
        val equipment = mockk<AircraftEquipment>()
        every { equipmentRepo.save(equipment) } returns equipment

        // Act
        val result = equipmentService.save(equipment)

        // Assert
        assertThat(result).isEqualTo(equipment)
        verify { equipmentRepo.save(equipment) }
    }

}