package org.oldjopa.hls.integration

import org.oldjopa.hls.integration.configuration.TestContainersConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration(
    initializers = [ContainerContextInitializer::class],
    classes = [TestContainersConfig::class]
)
@Testcontainers(disabledWithoutDocker = true)
abstract class AbstractIntegrationTest {
    @Autowired
    protected lateinit var mockMvc: MockMvc
}

