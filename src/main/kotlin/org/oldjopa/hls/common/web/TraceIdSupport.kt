package org.oldjopa.hls.common.web

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*

object TraceIdHolder {
    private val tl = ThreadLocal<String?>()
    fun set(id: String) { tl.set(id); MDC.put("traceId", id) }
    fun clear() { tl.remove(); MDC.remove("traceId") }
    fun current(): String? = tl.get()
}

@Component
class TraceIdFilter : OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val headerName = "X-Trace-Id"
        val traceId = request.getHeader(headerName)?.takeIf { it.isNotBlank() } ?: UUID.randomUUID().toString()
        TraceIdHolder.set(traceId)
        try {
            response.setHeader(headerName, traceId)
            filterChain.doFilter(request, response)
        } finally {
            TraceIdHolder.clear()
        }
    }
}

