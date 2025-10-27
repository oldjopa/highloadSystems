package org.oldjopa.hls.service.aircraft

import org.oldjopa.hls.common.exception.NotFoundException
import org.oldjopa.hls.common.exception.ValidationException
import org.oldjopa.hls.dto.AircraftDto
import org.oldjopa.hls.dto.CreateAircraftEquipmentRequest
import org.oldjopa.hls.dto.CreateAircraftRequest
import org.oldjopa.hls.dto.CreateEngineRequest
import org.oldjopa.hls.dto.CreateTechPassportRequest
import org.oldjopa.hls.dto.UpdateAircraftRequest
import org.oldjopa.hls.model.aircraft.Aircraft
import org.oldjopa.hls.model.aircraft.AircraftEquipment
import org.oldjopa.hls.model.feature.Engine
import org.oldjopa.hls.model.feature.TechPassport
import org.oldjopa.hls.repository.aircraft.AircraftRepository
import org.oldjopa.hls.service.deal.DealService
import org.oldjopa.hls.service.user.UserService
import org.oldjopa.hls.utls.toDto
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.Instant

@Service
class AircraftService(
    private val aircraftRepository: AircraftRepository,
    private val equipmentService: AircraftEquipmentService,
    private val engineService: EngineService,
    private val techPassportService: TechPassportService,
    private val userService: UserService,
    private val dealService: DealService
) {
    fun get(id: Long): AircraftDto = aircraftRepository.findById(id).orElseThrow {
        NotFoundException("Aircraft $id not found")
    }.toDto()

    fun getEntity(id: Long): Aircraft = aircraftRepository.findById(id).orElseThrow {
        NotFoundException("Aircraft $id not found")
    }

    fun list(pageable: Pageable): List<AircraftDto> =
        aircraftRepository.findAll(pageable).map { it.toDto() }.content

    fun listFull() = aircraftRepository.findAllProjectedBy()

    fun getFull(id: Long) =
        aircraftRepository.findProjectedById(id)
            ?: throw IllegalArgumentException("Aircraft not found")

    @Transactional
    fun create(req: CreateAircraftRequest): AircraftDto {
        if (aircraftRepository.existsBySerialNumber(req.serialNumber)) {
            throw ValidationException("Serial number already exists: ${req.serialNumber}")
        }
        val type = equipmentService.get(req.typeId)
        val owner = userService.get(req.ownerId)
        val tp = req.techPassportId?.let {
            techPassportService.get(it)
        }
        val saved = aircraftRepository.save(
            Aircraft(
                type = type,
                techPassport = tp,
                owner = owner,
                serialNumber = req.serialNumber,
                registrationNumber = req.registrationNumber,
                listedPrice = req.listedPrice,
                currency = req.currency
            )
        )
        return saved.toDto()
    }

    fun createAircraftType(req: CreateAircraftEquipmentRequest): Long {
        val engine = engineService.get(req.engineId)
        val equipment = AircraftEquipment(
            manufacturer = req.manufacturer,
            model = req.model,
            variant = req.variant,
            description = req.description,
            engineCount = req.engineCount,
            engine = engine,
            maxSeats = req.maxSeats,
            maxTakeoffWeightKg = req.maxTakeoffWeightKg,
            rangeKm = req.rangeKm,
            cruiseSpeedKnots = req.cruiseSpeedKnots,
            pressurized = req.pressurized
        )
        val saved = equipmentService.save(equipment)
        return saved.id
    }

    fun createEngine(req: CreateEngineRequest): Long {
        val engine = Engine(
            name = req.name,
            type = req.type,
            power = req.power
        )
        val saved = engineService.save(engine)
        return saved.id
    }

    fun createTechPassport(req: CreateTechPassportRequest): Long {
        val tp = TechPassport(
            flightHours = req.flightHours,
            manufactureYear = req.manufactureYear,
            emptyWeightKg = req.emptyWeightKg,
            fuelCapacityL = req.fuelCapacityL,
            lengthM = req.lengthM,
            wingspanM = req.wingspanM,
            heightM = req.heightM,
            noiseCert = req.noiseCert,
            createdAt = Instant.now(),
            updatedAt = Instant.now()
        )
        val saved = techPassportService.save(tp)
        return saved.id
    }

    @Transactional
    fun update(id: Long, req: UpdateAircraftRequest): AircraftDto {
        val a = aircraftRepository.findById(id).orElseThrow { NotFoundException("Aircraft $id not found") }
        req.registrationNumber?.let { a.registrationNumber = it }
        req.ownerId?.let { ownerId ->
            a.owner = userService.get(ownerId)
        }
        req.listedPrice?.let {
            if (it < BigDecimal.ZERO) throw ValidationException("Listed price cannot be negative")
            a.listedPrice = it
        }
        req.currency?.let {
            if (a.listedPrice != null && it.isBlank()) throw ValidationException("Currency cannot be blank when listed price is set")
            a.currency = it
        }
        return a.toDto()
    }

    @Transactional
    fun delete(id: Long) {
        val aircraft = aircraftRepository.findById(id).orElseThrow { NotFoundException("Aircraft $id not found") }
        val techPassport = aircraft.techPassport
        if (techPassport != null) {
            techPassportService.delete(techPassport.id)
        }
//        dealService.deleteAllByAircraftId(id)
        aircraftRepository.deleteById(id)
    }
}