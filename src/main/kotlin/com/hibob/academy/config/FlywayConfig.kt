package com.hibob.academy.config

import jakarta.annotation.PostConstruct
import org.flywaydb.core.Flyway
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class FlywayConfig (private val dataSource: DataSource) {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    @PostConstruct
    fun start() {
        migrate()
    }

    fun migrate() {
        try {
            val flyway = Flyway.configure()
                .dataSource(dataSource)
                .outOfOrder(true)
                .load()
            flyway.migrate()
        } catch (e: Exception) {
            logger.error("Flyway migration failed", e)
            throw e
        }
    }
}