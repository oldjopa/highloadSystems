package org.oldjopa.hls.integration

import org.oldjopa.hls.integration.container.PostgresContainer
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.PostgreSQLContainer

class ContainerContextInitializer: ApplicationContextInitializer<ConfigurableApplicationContext>{

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        PostgresContainer.start(applicationContext)
    }
}