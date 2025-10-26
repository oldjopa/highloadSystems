package org.oldjopa.hls.integration.configuration

import org.springframework.boot.test.context.TestConfiguration

@TestConfiguration
class TestContainersConfig {

//    @Bean
//    fun postgresContainer(): PostgreSQLContainer<*> {
//        val container = PostgreSQLContainer("postgres:15.4-alpine").apply {
//            withDatabaseName("testdb")
//            withUsername("test")
//            withPassword("test")
//            start()
//        }
//
//        System.setProperty("spring.datasource.url", container.jdbcUrl)
//        System.setProperty("spring.datasource.username", container.username)
//        System.setProperty("spring.datasource.password", container.password)
//
//        return container
//    }
}