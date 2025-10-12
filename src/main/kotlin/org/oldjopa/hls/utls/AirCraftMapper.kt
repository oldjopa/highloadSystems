package org.oldjopa.hls.utls

import org.oldjopa.hls.dto.AircraftDto
import org.oldjopa.hls.model.aircraft.Aircraft


fun Aircraft.toDto() = AircraftDto(
    id = id,
    typeId = type.id,
    techPassportId = techPassport?.id,
    ownerId = owner.id,
    serialNumber = serialNumber,
    registrationNumber = registrationNumber,
    listedPrice = listedPrice,
    currency = currency
)