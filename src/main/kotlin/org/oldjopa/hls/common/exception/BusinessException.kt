package org.oldjopa.hls.common.exception

open class BusinessException(
    message: String,
    val code: String = "BUSINESS_ERROR"
) : RuntimeException(message)

class NotFoundException(message: String, code: String = "NOT_FOUND") : BusinessException(message, code)
class ValidationException(message: String, code: String = "VALIDATION_ERROR") : BusinessException(message, code)

