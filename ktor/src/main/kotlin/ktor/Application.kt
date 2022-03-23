package ktor

import io.ktor.application.Application
import ktor.config.configureSecurity
import ktor.config.initializeDatabaseConnection
import ktor.config.plugins.configureMicrometer
import ktor.config.plugins.configureSerialization
import ktor.config.plugins.initializeKodein
import ktor.controller.configureRouting

fun main(args: Array<String>): Unit = io.ktor.server.cio.EngineMain.main(args)

fun Application.module() {
    initializeDatabaseConnection()
    configureSerialization()
    configureSecurity()

    val kodein = initializeKodein(environment)

    configureMicrometer(kodein)
    configureRouting(kodein)
}
