package org.oldjopa.hls.integration.aircraft

import org.junit.jupiter.api.Test
import org.oldjopa.hls.integration.AbstractIntegrationTest
import org.oldjopa.hls.testUtils.TestUtils.fromResources
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.get

class AircraftApiIntegrationTest : AbstractIntegrationTest() {

    @Test
    fun `should return aircraft list`() {
        val responseJson = fromResources("/json/aircraft/response_get_all_01.json")
        mockMvc.get("/api/aircrafts")
            .andExpect {
                status { isOk() }
                content { json(responseJson) }
            }
    }

    @Test
    @Sql(scripts = ["/sql/aircraft/post_aircraft_after.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    fun `should create aircraft`() {
        val requestJson = fromResources("/json/aircraft/create_request_01.json")

        mockMvc.post("/api/aircrafts") {
            contentType = MediaType.APPLICATION_JSON
            content = requestJson
        }.andExpect {
            status { isCreated() }
        }
    }
}
