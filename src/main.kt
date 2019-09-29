import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Locations
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

@KtorExperimentalLocationsAPI
fun main(args: Array<String>) {
    val env = applicationEngineEnvironment {
        module {
            main()
        }
        // Private API
        connector {
            host = "127.0.0.1"
            port = 3000
        }
        // Private API
        connector {
            host = "0.0.0.0"
            port = 8080
        }
    }
    embeddedServer(Netty, env).start(wait = true)
}

@KtorExperimentalLocationsAPI
fun Application.main() {

    install(ContentNegotiation) {
        gson()
    }
    install(StatusPages)
    install(CallLogging)
    install(Locations)

    routing {
        get("/") {
            if (call.request.local.port == 8080) {
                call.respondText("Connected to public api")
            } else {
                call.respondText("Connected to private api")
            }
        }
        get("/demo") {
            call.respondText("HELLO WORLD!")
        }
    }
}