package no.nav.plattform

import io.ktor.application.call
import io.ktor.application.install
import io.ktor.metrics.micrometer.MicrometerMetrics
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.util.KtorExperimentalAPI
import io.micrometer.core.instrument.Clock
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import io.micrometer.core.instrument.binder.system.FileDescriptorMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import io.prometheus.client.CollectorRegistry

@KtorExperimentalAPI
fun main() {
    val meterRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT, CollectorRegistry.defaultRegistry, Clock.SYSTEM)
    embeddedServer(CIO, 8080) {
        install(MicrometerMetrics) {
            registry = meterRegistry

            meterBinders = listOf(
                    ClassLoaderMetrics(),
                    FileDescriptorMetrics(),
                    JvmGcMetrics(),
                    JvmMemoryMetrics(),
                    JvmThreadMetrics(),
                    ProcessorMetrics()
            )
        }

        routing {
            get("/is_alive") {
                call.respondText("I'm alive!")
            }

            get("/is_ready") {
                call.respondText("I'm ready!")
            }

            get("/prometheus") {
                call.respondText(meterRegistry.scrape())
            }
        }
    }.start(wait = true)
}
