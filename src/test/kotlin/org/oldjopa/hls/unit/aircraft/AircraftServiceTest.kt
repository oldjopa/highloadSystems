package org.oldjopa.hls.unit.aircraft

import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.oldjopa.hls.common.exception.NotFoundException
import org.oldjopa.hls.common.exception.ValidationException
import org.oldjopa.hls.dto.*
import org.oldjopa.hls.model.aircraft.Aircraft
import org.oldjopa.hls.model.aircraft.AircraftEquipment
import org.oldjopa.hls.model.feature.*
import org.oldjopa.hls.model.user.User
import org.oldjopa.hls.repository.aircraft.*
import org.oldjopa.hls.repository.feature.*
import org.oldjopa.hls.repository.user.UserRepository
import org.oldjopa.hls.service.AircraftService
import java.math.BigDecimal
import java.time.Instant
import java.util.Optional
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


class AircraftServiceTest {

    private val aircraftRepository = mockk<AircraftRepository>()
    private val equipmentRepository = mockk<AircraftEquipmentRepository>()
    private val engineRepository = mockk<EngineRepository>()
    private val techPassportRepository = mockk<TechPassportRepository>()
    private val userRepository = mockk<UserRepository>()

    private val service = AircraftService(
        aircraftRepository,
        equipmentRepository,
        engineRepository,
        techPassportRepository,
        userRepository
    )

    @AfterEach
    fun tearDown() = clearAllMocks()

    private fun user(id: Long) = User(
        id = id,
        email = "u$id@example.com",
        hashedPassword = "pwd",
        firstName = "F$id",
        lastName = "L$id",
        roles = mutableSetOf()
    )

    private fun equipment(id: Long) = AircraftEquipment(
        id = id,
        manufacturer = "M",
        model = "Model",
        variant = null,
        description = null,
        engineCount = 2,
        engine = Engine(
            id = 1,
            name = "E1",
            type = EngineType.JET,
            power = 1000.0
        ),
        maxSeats = 10,
        maxTakeoffWeightKg = 10000,
        rangeKm = 3000,
        cruiseSpeedKnots = 400,
        pressurized = true
    )

    private fun techPassport(id: Long) = TechPassport(
        id = id,
        manufactureYear = 2020,
        emptyWeightKg = 5000,
        fuelCapacityL = 2000,
        lengthM = 20.0,
        wingspanM = 25.0,
        heightM = 6.0,
        noiseCert = "Stage 3",
        createdAt = Instant.now(),
        updatedAt = Instant.now(),
        flightHours = 100.0,
        features = mutableSetOf(Feature(name = "1"))
    )

    @Test
    fun `create aircraft success`() {
        // Arrange
        val req = CreateAircraftRequest(
            serialNumber = "SN001",
            registrationNumber = "REG001",
            ownerId = 1,
            typeId = 1,
            techPassportId = 1,
            listedPrice = BigDecimal.valueOf(1_000_000),
            currency = "USD"
        )
        every { aircraftRepository.existsBySerialNumber(req.serialNumber) } returns false
        every { equipmentRepository.findById(req.typeId) } returns Optional.of(equipment(1))
        every { userRepository.findById(req.ownerId) } returns Optional.of(user(1))
        every { techPassportRepository.findById(req.techPassportId!!) } returns Optional.of(techPassport(1))
        val savedSlot = slot<Aircraft>()
        every { aircraftRepository.save(capture(savedSlot)) } answers {
            Aircraft(
                id = 1,
                serialNumber = savedSlot.captured.serialNumber,
                registrationNumber = savedSlot.captured.registrationNumber,
                type = savedSlot.captured.type,
                owner = savedSlot.captured.owner,
                techPassport = savedSlot.captured.techPassport,
                listedPrice = savedSlot.captured.listedPrice,
                currency = savedSlot.captured.currency
            )
        }

        // Act
        val dto = service.create(req)

        // Assert
        assertEquals(1, dto.id)
        assertEquals("SN001", dto.serialNumber)
        verify { aircraftRepository.save(any()) }
    }

    @Test
    fun `create aircraft with existing serial throws`() {
        // Arrange
        every { aircraftRepository.existsBySerialNumber("SN001") } returns true
        val req = CreateAircraftRequest(
            serialNumber = "SN001",
            registrationNumber = "REG001",
            ownerId = 1,
            typeId = 1,
            techPassportId = null,
            listedPrice = BigDecimal.valueOf(1_000_000),
            currency = "USD"
        )

        // Act & Assert
        assertFailsWith<ValidationException> { service.create(req) }
    }

    @Test
    fun `get aircraft success`() {
        // Arrange
        val a = Aircraft(
            id = 1,
            serialNumber = "SN001",
            registrationNumber = "REG001",
            type = equipment(1),
            owner = user(1),
            techPassport = null,
            listedPrice = BigDecimal.ONE,
            currency = "USD"
        )
        every { aircraftRepository.findById(1) } returns Optional.of(a)

        // Act
        val dto = service.get(1)

        // Assert
        assertEquals(1, dto.id)
        assertEquals("SN001", dto.serialNumber)
    }

    @Test
    fun `get aircraft not found throws`() {
        // Arrange
        every { aircraftRepository.findById(1) } returns Optional.empty()

        // Act & Assert
        assertFailsWith<NotFoundException> { service.get(1) }
    }

    @Test
    fun `update aircraft owner`() {
        // Arrange
        val a = Aircraft(
            id = 1,
            serialNumber = "SN001",
            registrationNumber = "REG001",
            type = equipment(1),
            owner = user(1),
            techPassport = null,
            listedPrice = BigDecimal.ONE,
            currency = "USD"
        )
        every { aircraftRepository.findById(1) } returns Optional.of(a)
        every { userRepository.findById(2) } returns Optional.of(user(2))
        val req = UpdateAircraftRequest(ownerId = 2)

        // Act
        val dto = service.update(1, req)

        // Assert
        assertEquals(2, dto.ownerId)
    }

@Test
fun `delete aircraft success`() {
    // Arrange
    val a = Aircraft(
        id = 1,
        serialNumber = "SN001",
        registrationNumber = "REG001",
        type = equipment(1),
        owner = user(1),
        techPassport = null,
        listedPrice = BigDecimal.ONE,
        currency = "USD"
    )
    every { aircraftRepository.existsById(1) } returns true
    every { aircraftRepository.findById(1) } returns Optional.of(a)
    every { aircraftRepository.deleteById(1) } just Runs

    // Act
    service.delete(1)

    // Assert
    verify { aircraftRepository.deleteById(1) }
}

@Test
fun `delete aircraft not found throws`() {
    // Arrange
    every { aircraftRepository.existsById(1) } returns false
    every { aircraftRepository.findById(1) } returns Optional.empty()

    // Act & Assert
    assertFailsWith<NotFoundException> { service.delete(1) }
}

    @Test
    fun `create engine success`() {
        // Arrange
        val req = CreateEngineRequest(
            name = "TurboJet X",
            type = EngineType.JET,
            power = 1500.0
        )
        val slot = slot<Engine>()
        every { engineRepository.save(capture(slot)) } answers { slot.captured }

        // Act
        val id = service.createEngine(req)

        // Assert
        assertEquals("TurboJet X", slot.captured.name)
        assertEquals(EngineType.JET, slot.captured.type)
        assertEquals(1500.0, slot.captured.power)
        verify { engineRepository.save(any()) }
    }

    @Test
    fun `create tech passport success`() {
        // Arrange
        val req = CreateTechPassportRequest(
            flightHours = 200.0,
            manufactureYear = 2015,
            emptyWeightKg = 6000,
            fuelCapacityL = 3000,
            lengthM = 20.0,
            wingspanM = 25.0,
            heightM = 5.5,
            noiseCert = "Stage 4"
        )
        val slot = slot<TechPassport>()
        every { techPassportRepository.save(capture(slot)) } answers { slot.captured }

        // Act
        val id = service.createTechPassport(req)

        // Assert
        assertEquals(2015, slot.captured.manufactureYear)
        assertEquals(200.0, slot.captured.flightHours)
        assertEquals("Stage 4", slot.captured.noiseCert)
        verify { techPassportRepository.save(any()) }
    }

    @Test
    fun `create aircraft type success`() {
        // Arrange
        val engine = Engine(
            id = 2,
            name = "E2000",
            type = EngineType.TURBOPROP,
            power = 900.0
        )
        every { engineRepository.findById(2) } returns Optional.of(engine)

        val req = CreateAircraftEquipmentRequest(
            manufacturer = "Boeing",
            model = "X200",
            variant = "A",
            description = "Fast aircraft",
            engineCount = 2,
            engineId = 2,
            maxSeats = 10,
            maxTakeoffWeightKg = 8000,
            rangeKm = 2500,
            cruiseSpeedKnots = 400,
            pressurized = true
        )
        val slot = slot<AircraftEquipment>()
        every { equipmentRepository.save(capture(slot)) } answers { slot.captured }

        // Act
        val id = service.createAircraftType(req)

        // Assert
        assertEquals("Boeing", slot.captured.manufacturer)
        assertEquals("X200", slot.captured.model)
        assertEquals(2, slot.captured.engineCount)
        assertEquals(engine, slot.captured.engine)
        verify { equipmentRepository.save(any()) }
    }
}
