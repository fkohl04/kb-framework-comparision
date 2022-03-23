package ktor.config.plugins

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.metrics.micrometer.MicrometerMetrics
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import org.kodein.di.LazyKodein
import org.kodein.di.generic.instance

fun Application.configureMicrometer(kodein: LazyKodein) {
    val appMicrometerRegistry by kodein.instance<PrometheusMeterRegistry>()

    install(MicrometerMetrics) {
        registry = appMicrometerRegistry

        timers { _, _ -> tag("framework", "ktor") }
    }

    routing {
        get("/metrics") {
            call.respond(appMicrometerRegistry.scrape())
        }
    }
}
