package ktor.config.extension

import io.ktor.application.Application
import io.ktor.application.ApplicationEnvironment

fun ApplicationEnvironment.getPropertyOrEmptyString(property: String): String {
    return config.propertyOrNull(property)?.getString() ?: ""
}
