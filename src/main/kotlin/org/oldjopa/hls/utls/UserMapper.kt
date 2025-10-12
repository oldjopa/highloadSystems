package org.oldjopa.hls.utls

import org.oldjopa.hls.dto.UserDto
import org.oldjopa.hls.model.user.User

fun User.toDto() = UserDto(
    id = id,
    email = email,
    phone = phone,
    firstName = firstName,
    lastName = lastName
)

