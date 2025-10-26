package org.oldjopa.hls.unit.aircraft

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.oldjopa.hls.model.feature.Engine
import org.oldjopa.hls.repository.feature.EngineRepository
import org.oldjopa.hls.service.aircraft.EngineService
import java.util.Optional

class EngineServiceTest {
    private val engineRepo = mockk<EngineRepository>()
    private val engineService = EngineService(engineRepo)

    @Test
    fun `get engine should return engine`() {
        // Arrange
        val engine = Engine(name = "E1", type = mockk(), power = 100.0)
        every { engineRepo.findById(1) } returns Optional.of(engine)

        // Act
        val result = engineService.get(1)

        // Assert
        assertThat(result).isEqualTo(engine)
    }

    @Test
    fun `save engine should persist engine`() {
        // Arrange
        val engine = Engine(name = "E2", type = mockk(), power = 200.0)
        every { engineRepo.save(engine) } returns engine

        // Act
        val result = engineService.save(engine)

        // Assert
        assertThat(result).isEqualTo(engine)
        verify { engineRepo.save(engine) }
    }
}