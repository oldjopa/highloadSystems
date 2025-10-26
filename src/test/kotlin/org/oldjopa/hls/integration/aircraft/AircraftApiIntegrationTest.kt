package org.oldjopa.hls.integration.aircraft

import org.junit.jupiter.api.Test
import org.oldjopa.hls.integration.AbstractIntegrationTest
import org.oldjopa.hls.testUtils.TestUtils.fromResources
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post

class AircraftApiIntegrationTest : AbstractIntegrationTest() {

    @Test
    fun `should return aircraft list`() {
        val responseJson = fromResources("/json/aircraft/response_get_all_01.json")
        mockMvc.get("/api/aircrafts")
            .andExpect {
                status { isOk() }
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

    @Test
    @Sql(
        scripts = ["/sql/aircraft/pre_aircraft_with_data.sql"],
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
        scripts = ["/sql/aircraft/post_aircraft_after.sql"],
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    fun `should get aircraft by id`() {
        mockMvc.get("/api/aircrafts/10")
            .andExpect {
                status { isOk() }
                jsonPath("$.id") { value(10) }
                jsonPath("$.serialNumber") { value("SN-001") }
            }
    }

    @Test
    @Sql(
        scripts = ["/sql/aircraft/pre_aircraft_with_data.sql"],
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
        scripts = ["/sql/aircraft/post_aircraft_after.sql"],
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    fun `should update aircraft`() {
        val requestJson = """{
          "registrationNumber": "RA-NEW-001",
          "ownerId": 1,
          "listedPrice": 11000,
          "currency": "str"
        }"""

        mockMvc.patch("/api/aircrafts/10") {
            contentType = MediaType.APPLICATION_JSON
            content = requestJson
        }.andExpect {
            status { isOk() }
            jsonPath("$.registrationNumber") { value("RA-NEW-001") }
        }
    }

    @Test
    @Sql(
        scripts = ["/sql/aircraft/pre_aircraft_with_data.sql"],
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
        scripts = ["/sql/aircraft/post_aircraft_after.sql"],
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    fun `should delete aircraft`() {
        mockMvc.delete("/api/aircrafts/10")
            .andExpect {
                status { isNoContent() }
            }
    }
}
