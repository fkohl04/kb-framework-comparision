package ktor.config

import io.ktor.application.Application
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.authentication
import io.ktor.auth.basic
import ktor.config.Security.CUSTOM_BASIC_AUTH

object Security {
    const val CUSTOM_BASIC_AUTH = "basic"
}

fun Application.configureSecurity() {
    authentication {
        basic(CUSTOM_BASIC_AUTH) {
            validate { credentials ->
                if (credentials.name == "user" && credentials.password == "password") {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }
    }
}
