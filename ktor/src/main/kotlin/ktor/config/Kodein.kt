package ktor.config.plugins

import io.ktor.application.ApplicationEnvironment
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import ktor.client.RandomDataClient
import ktor.config.extension.getPropertyOrEmptyString
import ktor.database.RandomsRepository
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

fun initializeKodein(environment: ApplicationEnvironment) = Kodein.lazy {
    val serverUrl = environment.getPropertyOrEmptyString("server.url")

    bind<RandomDataClient>() with singleton { RandomDataClient(serverUrl) }
    bind<RandomsRepository>() with singleton { RandomsRepository() }
    bind<PrometheusMeterRegistry>() with singleton { PrometheusMeterRegistry(PrometheusConfig.DEFAULT) }
}
