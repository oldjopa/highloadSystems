package org.oldjopa.hls.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.oldjopa.hls.dto.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "Deals", description = "API для управления сделками: создание, изменение статусов, история и статусы")
@RequestMapping("/api/deals")
interface DealApi {

    @GetMapping
    @Operation(
        summary = "Получить все сделки",
        description = "Возвращает список всех сделок в системе",
        responses = [ApiResponse(responseCode = "200", description = "Список сделок", content = [Content(schema = Schema(implementation = DealDto::class))])]
    )
    fun getAll(pageable: Pageable): ResponseEntity<Page<DealDto>>

    @GetMapping("/{id}")
    @Operation(
        summary = "Получить сделку по ID",
        description = "Возвращает подробную информацию о сделке по её идентификатору",
        responses = [
            ApiResponse(responseCode = "200", description = "Сделка найдена", content = [Content(schema = Schema(implementation = DealDto::class))]),
            ApiResponse(responseCode = "404", description = "Сделка не найдена", content = [Content()])
        ]
    )
    fun get(
        @Parameter(description = "ID сделки", example = "1") @PathVariable id: Long
    ): DealDto

    @GetMapping("/{id}/history")
    @Operation(
        summary = "История изменений статусов сделки",
        description = "Возвращает хронологию изменений статусов для сделки",
        responses = [
            ApiResponse(responseCode = "200", description = "История найдена", content = [Content(schema = Schema(implementation = DealStatusHistoryDto::class))]),
            ApiResponse(responseCode = "404", description = "Сделка не найдена", content = [Content()])
        ]
    )
    fun history(
        @Parameter(description = "ID сделки", example = "1") @PathVariable id: Long
    ): List<DealStatusHistoryDto>

    @GetMapping("/statuses")
    @Operation(
        summary = "Получить все статусы сделок",
        description = "Возвращает список всех возможных статусов сделок",
        responses = [ApiResponse(responseCode = "200", description = "Список статусов", content = [Content(schema = Schema(implementation = DealStatusDto::class))])]
    )
    fun statuses(): List<DealStatusDto>

    @PostMapping
    @Operation(
        summary = "Создать новую сделку",
        description = "Создаёт сделку между покупателем и продавцом на основе переданных данных",
        responses = [
            ApiResponse(responseCode = "201", description = "Сделка успешно создана"),
            ApiResponse(responseCode = "404", description = "Пользователь, самолёт или статус не найдены"),
            ApiResponse(responseCode = "422", description = "Ошибка валидации", content = [Content()])
        ]
    )
    fun create(
        @Valid @RequestBody req: CreateDealRequest
    ): ResponseEntity<Any>

    @PostMapping("/{id}/status")
    @Operation(
        summary = "Изменить статус сделки",
        description = "Обновляет текущий статус сделки и добавляет запись в историю изменений",
        responses = [
            ApiResponse(responseCode = "200", description = "Статус успешно изменён", content = [Content(schema = Schema(implementation = DealDto::class))]),
            ApiResponse(responseCode = "404", description = "Сделка или статус не найдены"),
            ApiResponse(responseCode = "422", description = "Ошибка бизнес-валидации", content = [Content()])
        ]
    )
    fun changeStatus(
        @Parameter(description = "ID сделки", example = "1") @PathVariable id: Long,
        @Valid @RequestBody req: ChangeDealStatusRequest
    ): DealDto
}
