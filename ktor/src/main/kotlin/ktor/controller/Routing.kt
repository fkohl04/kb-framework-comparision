package ktor.controller

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing
import ktor.client.RandomDataClient
import ktor.config.Security
import ktor.database.RandomsRepository
import org.kodein.di.LazyKodein
import org.kodein.di.generic.instance

fun Application.configureRouting(kodein: LazyKodein) {

    val client by kodein.instance<RandomDataClient>()
    val repository = RandomsRepository()

    routing {
        authenticate(Security.CUSTOM_BASIC_AUTH) {
            route("/call-server") {
                get {
                    call.respond(client.fetchData())
                }
            }
            route("/call-database") {
                get {
                    call.respond(repository.findAllValues())
                }
            }
        }
    }
}
