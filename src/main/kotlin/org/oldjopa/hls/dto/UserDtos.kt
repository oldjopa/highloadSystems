package org.oldjopa.hls.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class UserDto(
    val id: Long,
    val email: String,
    val phone: String?,
    val firstName: String,
    val lastName: String
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CreateUserDto(
    @field:Email
    @field:NotBlank
    @field:Size(max = 320)
    val email: String,

    @field:Size(max = 32)
    val phone: String? = null,

//    @field:NotBlank
//    val hashedPassword: String,

    @field:NotBlank
    @field:Size(max = 100)
    val firstName: String,

    @field:NotBlank
    @field:Size(max = 100)
    val lastName: String,

    val roles: Set<String> = emptySet()
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class UpdateUserDto(
    @field:Size(max = 100)
    val firstName: String? = null,

    @field:Size(max = 100)
    val lastName: String? = null,

//    val hashedPassword: String? = null
)
