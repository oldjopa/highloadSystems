package org.oldjopa.hls.integration.deal


import org.junit.jupiter.api.Test
import org.oldjopa.hls.integration.AbstractIntegrationTest
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

class DealApiIntegrationTest : AbstractIntegrationTest() {

    @Test
    @Sql(
        scripts = ["/sql/deal/pre_deal_before.sql"],
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    fun `should create deal`() {
        val requestJson = """{
            "dealNumber": "123",
            "aircraftId": 1,
            "buyerId": 1,
            "sellerId": 2,
            "initialStatus": "NEW"
            }"""

        mockMvc.post("/api/deals") {
            contentType = MediaType.APPLICATION_JSON
            content = requestJson
        }.andExpect {
            status { isCreated() }
        }
    }

    @Test
    @Sql(
        scripts = ["/sql/deal/pre_deal_before.sql"],
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    fun `should get all deals`() {
        mockMvc.get("/api/deals")
            .andExpect {
                status { isOk() }
            }
    }

    @Test
    @Sql(
        scripts = ["/sql/deal/pre_deal_before.sql"],
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    fun `should get deal by id`() {
        mockMvc.get("/api/deals/1")
            .andExpect {
                status { isOk() }
                jsonPath("$.id") { value(1) }
                jsonPath("$.status.code") { value("NEW") }
            }
    }

    @Test
    @Sql(
        scripts = ["/sql/deal/pre_deal_before.sql"],
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    fun `should update deal status`() {
        val requestJson = """{
              "statusCode": "NEW",
              "changedBy": 1,
              "comment": "TEST"
            }"""

        mockMvc.post("/api/deals/1/status") {
            contentType = MediaType.APPLICATION_JSON
            content = requestJson
        }.andExpect {
            status { isOk() }
            jsonPath("$.status.code") { value("NEW") }
        }
    }
}
