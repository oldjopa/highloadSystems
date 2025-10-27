package org.oldjopa.hls.unit.deal

import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.oldjopa.hls.common.exception.NotFoundException
import org.oldjopa.hls.dto.ChangeDealStatusRequest
import org.oldjopa.hls.dto.CreateDealRequest
import org.oldjopa.hls.model.user.User
import org.oldjopa.hls.common.exception.ValidationException
import org.oldjopa.hls.model.aircraft.Aircraft
import org.oldjopa.hls.model.aircraft.AircraftEquipment
import org.oldjopa.hls.model.deal.Deal
import org.oldjopa.hls.model.deal.DealStatus
import org.oldjopa.hls.model.feature.Engine
import org.oldjopa.hls.model.feature.EngineType
import org.oldjopa.hls.repository.deal.DealRepository
import org.oldjopa.hls.service.aircraft.AircraftService
import org.oldjopa.hls.service.deal.DealService
import org.oldjopa.hls.service.deal.DealStatusHistoryService
import org.oldjopa.hls.service.deal.DealStatusService
import org.oldjopa.hls.service.user.UserService
import java.math.BigDecimal
import java.time.Instant
import java.util.*

class DealServiceTest {

    private val dealRepository = mockk<DealRepository>()
    private val dealStatusService = mockk<DealStatusService>()
    private val historyService = mockk<DealStatusHistoryService>()
    private val userService = mockk<UserService>()
    private val aircraftService = mockk<AircraftService>()

    private val service =
        DealService(dealRepository, dealStatusService, historyService, userService, aircraftService)

    @AfterEach
    fun tearDown() { clearAllMocks() }

    @Test
    fun `create deal success`() {
        // Arrange
        val buyer = user(10)
        val seller = user(20)
        val status = DealStatus("NEW", "New", null, 10, false)
        val aircraft = aircraft(100, seller)
        val req = CreateDealRequest("D-1", aircraft.id, buyer.id, seller.id, status.code)

        every { dealRepository.existsByDealNumber("D-1") } returns false
        every { aircraftService.getEntity(aircraft.id) } returns aircraft
        every { userService.get(buyer.id) } returns buyer
        every { userService.get(seller.id) } returns seller
        every { dealStatusService.get(status.code) } returns status
        val persistedDealSlot = slot<Deal>()
        every { dealRepository.save(capture(persistedDealSlot)) } answers {
            val d = persistedDealSlot.captured
            Deal(
                id = 1,
                dealNumber = d.dealNumber,
                aircraft = d.aircraft,
                buyer = d.buyer,
                seller = d.seller,
                status = d.status,
                isActive = d.isActive,
                closedAt = d.closedAt,
                createdAt = Instant.now(),
                updatedAt = Instant.now()
            )
        }
        every { historyService.save(any()) } answers { firstArg() }

        // Act
        val dto = service.create(req)

        // Assert
        assertEquals(1, dto.id)
        assertEquals("D-1", dto.dealNumber)
        assertEquals(status.code, dto.status.code)
        assertEquals(buyer.id, dto.buyer?.id)
        assertEquals(seller.id, dto.seller?.id)
        assertEquals(aircraft.id, dto.aircraft?.id)
        verify(exactly = 1) { historyService.save(any()) }
    }

    @Test
    fun `create deal duplicate number throws`() {
        // Arrange
        val req = CreateDealRequest("D-1", 1, 2, 3, "NEW")
        every { dealRepository.existsByDealNumber("D-1") } returns true

        // Act & Assert
        val ex = assertThrows(ValidationException::class.java) { service.create(req) }
        assertTrue(ex.message!!.contains("already exists"))
        verify(exactly = 0) { dealRepository.save(any()) }
    }

    @Test
    fun `change status to terminal closes deal`() {
        // Arrange
        val buyer = user(10)
        val seller = user(20)
        val nonTerminal = DealStatus("NEW", "New", null, 10, false)
        val terminal = DealStatus("COMPLETED", "Completed", null, 40, true)
        val aircraft = aircraft(100, seller)
        val deal = Deal(
            id = 5,
            dealNumber = "D-1",
            aircraft = aircraft,
            buyer = buyer,
            seller = seller,
            status = nonTerminal,
            createdAt = Instant.now(),
            updatedAt = Instant.now()
        )

        every { dealRepository.findById(5) } returns Optional.of(deal)
        every { dealStatusService.get(terminal.code) } returns terminal
        every { userService.get(buyer.id) } returns buyer
        every { historyService.save(any()) } answers { firstArg() }

        // Act
        val dto = service.changeStatus(5, ChangeDealStatusRequest(terminal.code, buyer.id, "Done"))

        // Assert
        assertEquals(terminal.code, dto.status.code)
        assertFalse(dto.isActive)
        assertNotNull(dto.closedAt)
    }

    @Test
    fun `change status when already terminal throws`() {
        // Arrange
        val terminal = DealStatus("CLOSED", "Closed", null, 10, true)
        val deal = Deal(
            id = 1,
            dealNumber = "D-1",
            buyer = user(1),
            seller = user(2),
            aircraft = aircraft(1, user(2)),
            status = terminal
        )
        every { dealRepository.findById(1) } returns Optional.of(deal)

        val newStatus = DealStatus("NEW", "New", null, 0, false)
        every { dealStatusService.get("NEW") } returns newStatus

        // Act & Assert
        val ex = assertThrows(ValidationException::class.java) {
            service.changeStatus(1, ChangeDealStatusRequest("NEW", 1, "Try change"))
        }
        assertTrue(ex.message!!.contains("already in terminal"))
    }

    @Test
    fun `get deal not found throws`() {
        // Arrange
        every { dealRepository.findById(1) } returns Optional.empty()

        // Act & Assert
        assertThrows(NotFoundException::class.java) { service.get(1) }
    }

    @Test
    fun `list deals returns paged dto list`() {
        // Arrange
        val deal = Deal(
            id = 1,
            dealNumber = "D-1",
            buyer = user(1),
            seller = user(2),
            aircraft = aircraft(1, user(2)),
            status = DealStatus("NEW", "New", null, 10, false)
        )
        val pageable = mockk<org.springframework.data.domain.Pageable>()
        val page = org.springframework.data.domain.PageImpl(listOf(deal))
        every { dealRepository.findAll(pageable) } returns page

        // Act
        val result = service.list(pageable)

        // Assert
        assertEquals(1, result.content.size)
        assertEquals("D-1", result.content[0].dealNumber)
        verify { dealRepository.findAll(pageable) }
    }

    private fun user(id: Long) = User(
        id = id,
        email = "u$id@example.com",
        hashedPassword = "pwd",
        firstName = "F$id",
        lastName = "L$id",
        roles = mutableSetOf()
    )

    private fun aircraft(id: Long, owner: User): Aircraft {
        val engine = Engine(id = 1, name = "E1", type = EngineType.JET, power = 1000.0)
        val equipment = AircraftEquipment(
            id = 1,
            manufacturer = "M",
            model = "Model",
            variant = null,
            description = null,
            engineCount = 2,
            engine = engine,
            maxSeats = 10,
            maxTakeoffWeightKg = 10000,
            rangeKm = 3000,
            cruiseSpeedKnots = 400,
            pressurized = true
        )
        return Aircraft(
            id = id,
            type = equipment,
            techPassport = null,
            owner = owner,
            serialNumber = "SN$id",
            registrationNumber = "REG$id",
            listedPrice = BigDecimal.valueOf(1_000_000),
            currency = "USD"
        )
    }

}
