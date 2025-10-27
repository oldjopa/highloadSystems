package org.oldjopa.hls.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.v3.core.jackson.ModelResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerJacksonConfig(
    private val objectMapper: ObjectMapper
) {
    @Bean
    fun modelResolver(): ModelResolver = ModelResolver(objectMapper)
}