package vertx.demo

import io.vertx.config.ConfigRetriever
import io.vertx.core.AbstractVerticle
import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.core.VertxOptions
import io.vertx.ext.web.Router
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.client.WebClientOptions
import io.vertx.ext.web.codec.BodyCodec
import io.vertx.jdbcclient.JDBCConnectOptions
import io.vertx.jdbcclient.JDBCPool
import io.vertx.micrometer.MicrometerMetricsOptions
import io.vertx.micrometer.PrometheusScrapingHandler
import io.vertx.micrometer.VertxPrometheusOptions
import io.vertx.sqlclient.PoolOptions


fun main() {
    val vertx = configureVertxWithPrometheus()
    vertx.deployVerticle(MainVerticle::class.java, DeploymentOptions().setInstances(4))
}

private fun configureVertxWithPrometheus() = Vertx.vertx(
    VertxOptions().setMetricsOptions(
        MicrometerMetricsOptions()
            .setPrometheusOptions(VertxPrometheusOptions().setEnabled(true))
            .setEnabled(true)
            .setJvmMetricsEnabled(true)
    )
)

class MainVerticle : AbstractVerticle() {

    override fun start() {

        val config = ConfigRetriever.create(vertx)

        lateinit var thirdPartyUrl: String
        lateinit var port: String
        lateinit var dbUrl: String
        lateinit var dbUser: String
        lateinit var dbPassword: String

        config.getConfig { json ->
            val result = json.result()
            thirdPartyUrl = result.getString("THIRD_PARTY_URL", "google.com")
            port = result.getString("PORT", "8087")
            dbUrl = result.getString("DB_URL", "jdbc:postgresql://localhost:5432/demo")
            dbUser = result.getString("DB_USER", "demo")
            dbPassword = result.getString("DB_PASSWORD", "pass")
        }

        val jdbcPool: JDBCPool = JDBCPool.pool(
            vertx,
            JDBCConnectOptions()
                .setJdbcUrl(dbUrl)
                .setUser(dbUser)
                .setPassword(dbPassword),
            PoolOptions().setMaxSize(16)
        )

        val client = WebClient.create(
            vertx,
            WebClientOptions()
                .setMaxPoolSize(500)
                .setDefaultHost(thirdPartyUrl)
        )

        val router = vertx.configureRouter(client, jdbcPool)

        vertx.createHttpServer()
            .requestHandler(router)
            .listen(port.toInt())
            .onSuccess { server ->
                println("HTTP server started on port " + server.actualPort())
            }
    }

    private fun Vertx.configureRouter(
        client: WebClient,
        pool: JDBCPool
    ): Router {

        val router = Router.router(this)

        router.route("/metrics").handler(PrometheusScrapingHandler.create())
        router.route("/call-server").handler { context ->
            client.get("/api/business_credit_card/random_card?size=2")
                .`as`(BodyCodec.string())
                .send { asyncResult ->
                    if (asyncResult.succeeded()) {
                        val body = asyncResult.result().body()
                        context.end("Fetched third party result:$body")
                    } else {
                        context.fail(500)
                    }
                }
        }
        router.route("/call-database").handler { context ->
            pool.query("SELECT * FROM randoms")
                .execute()
                .onSuccess {
                    context.end(it.mapNotNull { row -> row.getString("value") }.toString())
                }.onFailure {
                    context.fail(500)
                }
        }
        return router
    }
}
