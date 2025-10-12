package org.oldjopa.hls

import org.junit.jupiter.api.Test
import org.oldjopa.hls.integration.TestcontainersConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@Import(TestcontainersConfiguration::class)
@SpringBootTest
class HighloadsystemsApplicationTests {

    @Test
    fun contextLoads() {
    }

}
