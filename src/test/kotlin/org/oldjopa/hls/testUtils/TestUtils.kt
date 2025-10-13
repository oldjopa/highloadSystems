package org.oldjopa.hls.testUtils

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.nio.file.Files
import java.nio.file.Paths

object TestUtils {
    private val mapper = jacksonObjectMapper()

    fun fromResources(path: String): String {
        val resource = this::class.java.getResource(path)
            ?: throw IllegalArgumentException("Resource not found: $path")
        return Files.readString(Paths.get(resource.toURI()))
    }

    private inline fun <reified T> fromJsonResource(path: String): T =
        mapper.readValue(fromResources(path), T::class.java)
}
