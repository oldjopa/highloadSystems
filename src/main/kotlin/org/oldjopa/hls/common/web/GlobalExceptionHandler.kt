package org.oldjopa.hls.common.web

import jakarta.persistence.OptimisticLockException
import org.oldjopa.hls.common.exception.BusinessException
import org.oldjopa.hls.common.exception.NotFoundException
import org.oldjopa.hls.common.exception.ValidationException
import org.slf4j.LoggerFactory
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.servlet.NoHandlerFoundException
import java.time.Instant

@RestControllerAdvice
class GlobalExceptionHandler {
    private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    data class ErrorResponse(
        val timestamp: Instant = Instant.now(),
        val status: Int,
        val error: String,
        val code: String,
        val message: String?,
        val path: String?,
        val traceId: String? = TraceIdHolder.current()
    )

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(ex: NotFoundException, req: ServletWebRequest) = build(HttpStatus.NOT_FOUND, ex, req)

    @ExceptionHandler(ValidationException::class)
    fun handleValidation(ex: ValidationException, req: ServletWebRequest) = build(HttpStatus.UNPROCESSABLE_ENTITY, ex, req)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleBeanValidation(ex: MethodArgumentNotValidException, req: ServletWebRequest): ResponseEntity<ErrorResponse> {
        val msg = ex.bindingResult.fieldErrors.joinToString { "${it.field}: ${it.defaultMessage}" }
        val wrapped = ValidationException(msg)
        return build(HttpStatus.BAD_REQUEST, wrapped, req)
    }

    @ExceptionHandler(BusinessException::class)
    fun handleBusiness(ex: BusinessException, req: ServletWebRequest) = build(HttpStatus.BAD_REQUEST, ex, req)

    @ExceptionHandler(value = [OptimisticLockException::class, OptimisticLockingFailureException::class])
    fun handleOptimistic(ex: Exception, req: ServletWebRequest): ResponseEntity<ErrorResponse> {
        val be = BusinessException("Concurrent modification", code = "CONFLICT")
        return build(HttpStatus.CONFLICT, be, req)
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneric(ex: Exception, req: ServletWebRequest): ResponseEntity<ErrorResponse> {
        log.error("Unhandled exception", ex)
        return build(HttpStatus.INTERNAL_SERVER_ERROR, BusinessException(ex.message ?: "Unexpected error"), req)
    }

    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNoHandlerFound(ex: NoHandlerFoundException, req: ServletWebRequest): ResponseEntity<ErrorResponse> {
        log.warn("No handler found for: ${ex.httpMethod} ${ex.requestURL}")
        val path = req.request.requestURI
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ErrorResponse(
                status = HttpStatus.NOT_FOUND.value(),
                error = "Not Found",
                code = "NOT_FOUND",
                message = "Endpoint not found: $path",
                path = path
            )
        )
    }

    private fun build(status: HttpStatus, ex: BusinessException, req: ServletWebRequest): ResponseEntity<ErrorResponse> {
        if (status.is5xxServerError) log.error("Error response ${status.value()} ${ex.code}: ${ex.message}") else log.warn("Error response ${status.value()} ${ex.code}: ${ex.message}")
        val path = req.request.requestURI
        return ResponseEntity.status(status).body(
            ErrorResponse(
                status = status.value(),
                error = status.reasonPhrase,
                code = ex.code,
                message = ex.message,
                path = path
            )
        )
    }
}
