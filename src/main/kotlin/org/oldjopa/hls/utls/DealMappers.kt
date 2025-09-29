package org.oldjopa.hls.utls

import org.oldjopa.hls.dto.DealDto
import org.oldjopa.hls.dto.DealStatusDto
import org.oldjopa.hls.dto.DealStatusHistoryDto
import org.oldjopa.hls.model.DealStatusHistory
import org.oldjopa.hls.model.Deal
import org.oldjopa.hls.model.DealStatus

fun DealStatus.toDto() = DealStatusDto(
    code = code,
    name = name,
    description = description,
    orderIndex = orderIndex,
    isTerminal = isTerminal
)

fun Deal.toDto() = DealDto(
    id = id,
    dealNumber = dealNumber,
    aircraftId = aircraft.id,
    buyerId = buyer.id,
    sellerId = seller.id,
    status = status.toDto(),
    isActive = isActive,
    closedAt = closedAt
)

fun DealStatusHistory.toDto() = DealStatusHistoryDto(
    id = id,
    status = status.toDto(),
    changedByUserId = changedBy.id,
    changedAt = changedAt,
    comment = comment
)

fun List<DealStatusHistory>.toDto() = map { it.toDto() }

