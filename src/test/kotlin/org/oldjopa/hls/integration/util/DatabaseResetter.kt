package org.oldjopa.hls.integration.util

import liquibase.Liquibase
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.exception.LiquibaseException
import liquibase.resource.ClassLoaderResourceAccessor
import liquibase.Contexts
import liquibase.LabelExpression
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import javax.sql.DataSource

/**
 * Resets DB schema by dropping everything and re-applying Liquibase changelog.
 * Executed before each integration test class (see AbstractIntegrationTest).
 */
@Component
class DatabaseResetter(private val dataSource: DataSource) {
    private val log = LoggerFactory.getLogger(javaClass)
    private val changeLogPath = "db/changelog/changelog-master.yaml"


    @Synchronized
    fun reset() {
        dataSource.connection.use { conn ->
            val database = DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(JdbcConnection(conn))
            val resourceAccessor = ClassLoaderResourceAccessor()
            val liquibase = Liquibase(changeLogPath, resourceAccessor, database)
            try {
                log.info("[TEST] Dropping all database objects before test class ...")
                liquibase.dropAll()
                log.info("[TEST] Re-applying Liquibase changelog ...")
                liquibase.update(Contexts(), LabelExpression())
                log.info("[TEST] Database reset complete")
            } catch (e: LiquibaseException) {
                log.error("[TEST] Database reset failed", e)
                throw IllegalStateException("Failed to reset database for tests", e)
            }
        }
    }
}
