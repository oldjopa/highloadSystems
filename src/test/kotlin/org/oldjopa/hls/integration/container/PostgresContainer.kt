package org.oldjopa.hls.integration.container

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.PostgreSQLContainer

object PostgresContainer {
    private val postgres = PostgreSQLContainer("postgres:15.4-alpine").apply {
        withDatabaseName("testdb")
        withUsername("test")
        withPassword("test")
    }

    fun start(applicationContext: ConfigurableApplicationContext){
        postgres.start()

        TestPropertyValues.of(
            "spring.datasource.url=" + postgres.jdbcUrl,
            "spring.datasource.username=" + postgres.username,
            "spring.datasource.password=" + postgres.password,
        ).applyTo(applicationContext)
    }
}