package org.oldjopa.hls.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.oldjopa.hls.dto.*
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(
    name = "AirCrafts",
    description = "API для управления самолётами, их типами, двигателями и техническими паспортами"
)
@RequestMapping("/api/aircrafts")
interface AircraftApi {

    @GetMapping
    @Operation(
        summary = "Получить список всех самолётов",
        description = "Возвращает список всех зарегистрированных самолётов в кратком виде",
        responses = [ApiResponse(responseCode = "200", description = "Успешно", content = [Content(schema = Schema(implementation = AircraftDto::class))])]
    )
    fun getAll(pageable: Pageable): List<AircraftDto>

    @GetMapping("/{id}")
    @Operation(
        summary = "Получить самолёт по ID",
        description = "Возвращает полную информацию о самолёте по его идентификатору",
        responses = [
            ApiResponse(responseCode = "200", description = "Самолёт найден", content = [Content(schema = Schema(implementation = AircraftDto::class))]),
            ApiResponse(responseCode = "404", description = "Самолёт не найден", content = [Content()])
        ]
    )
    fun get(
        @Parameter(description = "ID самолёта", example = "1")
        @PathVariable id: Long
    ): AircraftDto

    @GetMapping("/{id}/full")
    @Operation(
        summary = "Получить самолёт с полной информацией",
        description = "Возвращает расширенную информацию о самолёте (связанное оборудование, паспорт и т.п.)",
        responses = [
            ApiResponse(responseCode = "200", description = "Самолёт найден", content = [Content()]),
            ApiResponse(responseCode = "404", description = "Самолёт не найден", content = [Content()])
        ]
    )
    fun getFull(
        @Parameter(description = "ID самолёта", example = "1")
        @PathVariable id: Long
    ): Any

    @GetMapping("/full")
    @Operation(
        summary = "Получить список всех самолётов (полная информация)",
        description = "Возвращает список всех самолётов с детальными данными и связанными сущностями",
        responses = [ApiResponse(responseCode = "200", description = "Успешно", content = [Content()])]
    )
    fun getAllFull(): List<Any>

    @PostMapping
    @Operation(
        summary = "Создать новый самолёт",
        description = "Создаёт запись о новом самолёте, привязывая его к владельцу и типу",
        responses = [
            ApiResponse(responseCode = "201", description = "Самолёт успешно создан"),
            ApiResponse(responseCode = "422", description = "Ошибка валидации", content = [Content()])
        ]
    )
    fun create(
        @Valid @RequestBody
        @Parameter(description = "Данные нового самолёта")
        req: CreateAircraftRequest
    ): ResponseEntity<Any>

    @PostMapping("/types")
    @Operation(
        summary = "Создать новый тип самолёта",
        description = "Добавляет описание типа самолёта (оборудование, характеристики, двигатель и т.д.)",
        responses = [ApiResponse(responseCode = "201", description = "Тип создан"), ApiResponse(responseCode = "422", description = "Ошибка валидации")]
    )
    fun createAircraftType(
        @Valid @RequestBody
        @Parameter(description = "Данные для создания типа самолёта")
        req: CreateAircraftEquipmentRequest
    ): ResponseEntity<Any>

    @PostMapping("/engines")
    @Operation(
        summary = "Создать новый тип двигателя",
        description = "Добавляет запись о типе двигателя, который может использоваться в самолётах",
        responses = [ApiResponse(responseCode = "201", description = "Двигатель создан"), ApiResponse(responseCode = "422", description = "Ошибка валидации")]
    )
    fun createEngine(
        @Valid @RequestBody
        @Parameter(description = "Данные для создания двигателя")
        req: CreateEngineRequest
    ): ResponseEntity<Any>

    @PostMapping("/tech-passports")
    @Operation(
        summary = "Создать технический паспорт",
        description = "Добавляет запись технического паспорта для самолёта с его характеристиками",
        responses = [ApiResponse(responseCode = "201", description = "Паспорт создан"), ApiResponse(responseCode = "422", description = "Ошибка валидации")]
    )
    fun createTechPassport(
        @Valid @RequestBody
        @Parameter(description = "Данные технического паспорта")
        req: CreateTechPassportRequest
    ): ResponseEntity<Any>

    @PatchMapping("/{id}")
    @Operation(
        summary = "Обновить данные самолёта",
        description = "Изменяет регистрационные данные или владельца самолёта",
        responses = [
            ApiResponse(responseCode = "200", description = "Данные успешно обновлены", content = [Content(schema = Schema(implementation = AircraftDto::class))]),
            ApiResponse(responseCode = "404", description = "Самолёт не найден")
        ]
    )
    fun update(
        @Parameter(description = "ID самолёта", example = "1")
        @PathVariable id: Long,
        @Valid @RequestBody
        @Parameter(description = "Данные для обновления самолёта")
        req: UpdateAircraftRequest
    ): AircraftDto

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Удалить самолёт",
        description = "Удаляет запись о самолёте по его идентификатору",
        responses = [
            ApiResponse(responseCode = "204", description = "Самолёт удалён"),
            ApiResponse(responseCode = "404", description = "Самолёт не найден")
        ]
    )
    fun delete(
        @Parameter(description = "ID самолёта", example = "1")
        @PathVariable id: Long
    )
}
