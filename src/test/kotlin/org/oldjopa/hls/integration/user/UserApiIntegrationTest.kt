package org.oldjopa.hls.integration.user

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.oldjopa.hls.integration.AbstractIntegrationTest
import org.oldjopa.hls.testUtils.TestUtils.fromResources
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

class UserApiIntegrationTest : AbstractIntegrationTest() {

    @Test
    fun `should return user by id`() {
        val responseJson = fromResources("/json/user/response_get_01.json")

        mockMvc.get("/api/users/1")
            .andExpect {
                status { isOk() }
                content { json(responseJson) }
            }
    }

    @Test
    @Disabled
    fun `should return all users`() {
        val responseJson = fromResources("/json/user/response_get_all_01.json")

        mockMvc.get("/api/users")
            .andExpect {
                status { isOk() }
                content { json(responseJson, strict = false) }
            }
    }


    @Test
    @Sql(
        scripts = ["/sql/user/clear_test_users.sql"],
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    fun `should create user`() {
        val requestJson = fromResources("/json/user/create_request_01.json")

        mockMvc.post("/api/users") {
            contentType = MediaType.APPLICATION_JSON
            content = requestJson
        }.andExpect {
            status { isCreated() }
            header { exists("Location") }
        }
    }

    @Test
    @Disabled
    @Sql(scripts = ["/sql/user/post_assign_roles_after.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    fun `should assign roles to user`() {
        val requestJson = fromResources("/json/user/assign_roles_request_01.json")
        val responseJson = fromResources("/json/user/assign_roles_response_01.json")

        mockMvc.post("/api/users/2/roles") {
            contentType = MediaType.APPLICATION_JSON
            content = requestJson
        }.andExpect {
            status { isOk() }
            content { json(responseJson) }
        }
    }
}
