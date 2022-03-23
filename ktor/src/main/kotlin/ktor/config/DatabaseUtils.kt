package ktor.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.Application
import ktor.config.DatabaseConfig.logger
import ktor.config.extension.getPropertyOrEmptyString
import ktor.database.Randoms
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.UUID

object DatabaseConfig {
    val logger: Logger = LoggerFactory.getLogger(DatabaseConfig.javaClass)
}

fun Application.initializeDatabaseConnection() {
    logger.info("Trying to connect to database")
    Database.connect(setupConnectionPool())
    transaction {
        SchemaUtils.create(Randoms)
    }
    transaction {
        (0..100).forEach {
            Randoms.insert {
                it[value] = UUID.randomUUID()
            }
        }
    }
    logger.info("Successfully connected to database")
}

private fun Application.setupConnectionPool(): HikariDataSource {
    val config = HikariConfig()
    config.driverClassName = environment.getPropertyOrEmptyString("datasource.driver")
    config.jdbcUrl = environment.getPropertyOrEmptyString("datasource.url")
    config.username = environment.getPropertyOrEmptyString("datasource.user")
    config.password = environment.getPropertyOrEmptyString("datasource.password")
    return HikariDataSource(config)
}
