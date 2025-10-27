package org.oldjopa.hls.utls

import org.oldjopa.hls.dto.DealDto
import org.oldjopa.hls.dto.DealStatusDto
import org.oldjopa.hls.dto.DealStatusHistoryDto
import org.oldjopa.hls.model.deal.Deal
import org.oldjopa.hls.model.deal.DealStatus
import org.oldjopa.hls.model.deal.DealStatusHistory

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
    aircraft = aircraft?.toDto(),
    buyer = buyer?.toDto(),
    seller = seller?.toDto(),
    status = status.toDto(),
    isActive = isActive,
    closedAt = closedAt
)

fun DealStatusHistory.toDto() = DealStatusHistoryDto(
    id = id,
    status = status.toDto(),
    changedByUserId = changedBy?.id,
    changedAt = changedAt,
    comment = comment
)