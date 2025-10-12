package org.oldjopa.hls.unit.deal

import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.oldjopa.hls.dto.ChangeDealStatusRequest
import org.oldjopa.hls.dto.CreateDealRequest
import org.oldjopa.hls.model.user.User
import org.oldjopa.hls.repository.user.UserRepository
import org.oldjopa.hls.common.exception.ValidationException
import org.oldjopa.hls.model.aircraft.Aircraft
import org.oldjopa.hls.model.aircraft.AircraftEquipment
import org.oldjopa.hls.model.deal.Deal
import org.oldjopa.hls.model.deal.DealStatus
import org.oldjopa.hls.model.feature.Engine
import org.oldjopa.hls.model.feature.EngineType
import org.oldjopa.hls.repository.aircraft.AircraftRepository
import org.oldjopa.hls.repository.deal.DealRepository
import org.oldjopa.hls.repository.deal.DealStatusHistoryRepository
import org.oldjopa.hls.repository.deal.DealStatusRepository
import org.oldjopa.hls.service.DealService
import java.math.BigDecimal
import java.time.Instant
import java.util.*
//#TODO rewrite
class DealServiceTest {

    private val dealRepository = mockk<DealRepository>()
    private val dealStatusRepository = mockk<DealStatusRepository>()
    private val historyRepository = mockk<DealStatusHistoryRepository>()
    private val userRepository = mockk<UserRepository>()
    private val aircraftRepository = mockk<AircraftRepository>()

    private val service =
        DealService(dealRepository, dealStatusRepository, historyRepository, userRepository, aircraftRepository)

    @AfterEach
    fun tearDown() { clearAllMocks() }

    @Test
    fun `create deal success`() {
        val buyer = user(id = 10)
        val seller = user(id = 20)
        val status = DealStatus("NEW", "New", null, 10, false)
        val aircraft = aircraft(id = 100, owner = seller)
        val req = CreateDealRequest("D-1", aircraft.id, buyer.id, seller.id, status.code)

        every { dealRepository.existsByDealNumber("D-1") } returns false
        every { aircraftRepository.findById(aircraft.id) } returns Optional.of(aircraft)
        every { userRepository.findById(buyer.id) } returns Optional.of(buyer)
        every { userRepository.findById(seller.id) } returns Optional.of(seller)
        every { dealStatusRepository.findById(status.code) } returns Optional.of(status)
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
        every { historyRepository.save(any()) } answers { firstArg() }

        val dto = service.create(req)

        assertEquals(1, dto.id)
        assertEquals("D-1", dto.dealNumber)
        assertEquals(status.code, dto.status.code)
        // nested objects now
        assertEquals(buyer.id, dto.buyer.id)
        assertEquals(seller.id, dto.seller.id)
        assertEquals(aircraft.id, dto.aircraft.id)
        verify(exactly = 1) { historyRepository.save(any()) }
    }

    @Test
    fun `create deal duplicate number throws`() {
        val req = CreateDealRequest("D-1", 1, 2, 3, "NEW")
        every { dealRepository.existsByDealNumber("D-1") } returns true
        val ex = assertThrows(ValidationException::class.java) { service.create(req) }
        assertTrue(ex.message!!.contains("already exists"))
        verify(exactly = 0) { dealRepository.save(any()) }
    }

    @Test
    fun `change status to terminal closes deal`() {
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
        every { dealStatusRepository.findById(terminal.code) } returns Optional.of(terminal)
        every { userRepository.findById(buyer.id) } returns Optional.of(buyer)
        every { historyRepository.save(any()) } answers { firstArg() }

        val dto = service.changeStatus(5, ChangeDealStatusRequest(terminal.code, buyer.id, "Done"))
        assertEquals(terminal.code, dto.status.code)
        assertFalse(dto.isActive)
        assertNotNull(dto.closedAt)
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
