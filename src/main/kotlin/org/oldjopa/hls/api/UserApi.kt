package org.oldjopa.hls.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.oldjopa.hls.dto.CreateUserDto
import org.oldjopa.hls.dto.UpdateUserDto
import org.oldjopa.hls.model.user.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(
    name = "Пользователи",
    description = "API для управления пользователями и их ролями"
)
@RequestMapping("/api/users")
interface UserApi {

    @GetMapping
    @Operation(
        summary = "Получить всех пользователей",
        description = "Возвращает список всех пользователей системы",
        responses = [ApiResponse(responseCode = "200", description = "Успешно", content = [Content(schema = Schema(implementation = User::class))])]
    )
    fun getAll(pageable: Pageable): Page<User>

    @GetMapping("/{id}")
    @Operation(
        summary = "Получить пользователя по ID",
        description = "Возвращает данные пользователя по его идентификатору",
        responses = [
            ApiResponse(responseCode = "200", description = "Пользователь найден", content = [Content(schema = Schema(implementation = User::class))]),
            ApiResponse(responseCode = "404", description = "Пользователь не найден")
        ]
    )
    fun get(
        @Parameter(description = "ID пользователя", example = "1")
        @PathVariable id: Long
    ): User

    @PostMapping
    @Operation(
        summary = "Создать нового пользователя",
        description = "Добавляет нового пользователя в систему с указанными ролями",
        responses = [
            ApiResponse(responseCode = "201", description = "Пользователь создан"),
            ApiResponse(responseCode = "422", description = "Ошибка валидации", content = [Content()])
        ]
    )
    fun create(
        @RequestBody
        @Parameter(description = "Данные нового пользователя")
        newUser: CreateUserDto
    ): ResponseEntity<Any>

    @PatchMapping("/{id}")
    @Operation(
        summary = "Обновить данные пользователя",
        description = "Изменяет имя, фамилию или пароль пользователя",
        responses = [
            ApiResponse(responseCode = "200", description = "Данные обновлены", content = [Content(schema = Schema(implementation = User::class))]),
            ApiResponse(responseCode = "404", description = "Пользователь не найден")
        ]
    )
    fun update(
        @Parameter(description = "ID пользователя", example = "1")
        @PathVariable id: Long,
        @RequestBody
        @Parameter(description = "Новые данные пользователя")
        dto: UpdateUserDto
    ): User

    @PostMapping("/{id}/roles")
    @Operation(
        summary = "Назначить роли пользователю",
        description = "Заменяет текущие роли пользователя на переданные",
        responses = [
            ApiResponse(responseCode = "200", description = "Роли успешно обновлены", content = [Content(schema = Schema(implementation = User::class))]),
            ApiResponse(responseCode = "404", description = "Пользователь не найден")
        ]
    )
    fun setRoles(
        @Parameter(description = "ID пользователя", example = "1")
        @PathVariable id: Long,
        @RequestBody
        @Parameter(description = "Список ролей (строковые идентификаторы)")
        roles: Set<String>
    ): User

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Удалить пользователя",
        description = "Удаляет пользователя по ID",
        responses = [
            ApiResponse(responseCode = "204", description = "Пользователь удалён"),
            ApiResponse(responseCode = "404", description = "Пользователь не найден")
        ]
    )
    fun delete(
        @Parameter(description = "ID пользователя", example = "1")
        @PathVariable id: Long
    )
}
