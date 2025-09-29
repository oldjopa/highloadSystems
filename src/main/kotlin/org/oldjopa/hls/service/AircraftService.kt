package org.oldjopa.hls.service

import org.oldjopa.hls.model.Aircraft
import org.oldjopa.hls.repository.AircraftEquipmentRepository
import org.oldjopa.hls.repository.AircraftRepository
import org.oldjopa.hls.repository.TechPassportRepository
import org.oldjopa.hls.repository.UserRepository
import org.oldjopa.hls.dto.CreateAircraftRequest
import org.oldjopa.hls.dto.UpdateAircraftRequest
import org.oldjopa.hls.dto.AircraftDto
import org.oldjopa.hls.common.exception.NotFoundException
import org.oldjopa.hls.common.exception.ValidationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.oldjopa.hls.utls.toDto

@Service
class AircraftService(
    private val aircraftRepository: AircraftRepository,
    private val equipmentRepository: AircraftEquipmentRepository,
    private val techPassportRepository: TechPassportRepository,
    private val userRepository: UserRepository
) {
    fun get(id: Long): AircraftDto = aircraftRepository.findById(id).orElseThrow {
        NotFoundException("Aircraft $id not found")
    }.toDto()

    fun list(): List<AircraftDto> = aircraftRepository.findAll().map { it.toDto() }

    @Transactional
    fun create(req: CreateAircraftRequest): AircraftDto {
        if (aircraftRepository.existsBySerialNumber(req.serialNumber)) {
            throw ValidationException("Serial number already exists: ${req.serialNumber}")
        }
        val type = equipmentRepository.findById(req.typeId).orElseThrow { NotFoundException("Equipment ${req.typeId} not found") }
        val owner = userRepository.findById(req.ownerId).orElseThrow { NotFoundException("User ${req.ownerId} not found") }
        val tp = req.techPassportId?.let { techPassportRepository.findById(it).orElseThrow { NotFoundException("TechPassport $it not found") } }
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

    @Transactional
    fun update(id: Long, req: UpdateAircraftRequest): AircraftDto {
        val a = aircraftRepository.findById(id).orElseThrow { NotFoundException("Aircraft $id not found") }
        req.registrationNumber?.let { a.registrationNumber = it }
        req.ownerId?.let { ownerId -> a.owner = userRepository.findById(ownerId).orElseThrow { NotFoundException("User $ownerId not found") } }
        // listedPrice & currency предполагаем тоже можно обновлять
        req.listedPrice?.let { /* entity field is val, skipping unless design changes */ }
        req.currency?.let { /* same note */ }
        return a.toDto()
    }

    @Transactional
    fun delete(id: Long) {
        if (!aircraftRepository.existsById(id)) throw NotFoundException("Aircraft $id not found")
        aircraftRepository.deleteById(id)
    }

    // Deprecated legacy command style kept for backward compatibility if something else references it
    @Deprecated("Use CreateAircraftRequest")
    data class CreateAircraftCmd(
        val typeId: Long,
        val techPassportId: Long?,
        val ownerId: Long,
        val serialNumber: String,
        val registrationNumber: String?,
        val listedPrice: java.math.BigDecimal?,
        val currency: String?
    )
    @Deprecated("Use UpdateAircraftRequest")
    data class UpdateAircraftCmd(
        val registrationNumber: String?,
        val ownerId: Long?
    )
}