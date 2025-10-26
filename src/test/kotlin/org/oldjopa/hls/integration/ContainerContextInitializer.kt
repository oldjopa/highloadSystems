package org.oldjopa.hls.integration

import org.oldjopa.hls.integration.container.PostgresContainer
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext

class ContainerContextInitializer: ApplicationContextInitializer<ConfigurableApplicationContext>{

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        PostgresContainer.start(applicationContext)
    }
}