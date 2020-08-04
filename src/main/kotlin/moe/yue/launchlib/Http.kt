package moe.yue.launchlib

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import java.time.LocalDateTime

val httpClient = HttpClient(OkHttp) {
    install(JsonFeature) {
        serializer = KotlinxSerializer(
            kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
                isLenient = true
            }
        )
    }
    install(Logging) {
        logger = SimpleLogger()
        level = LogLevel.INFO
    }
    install(HttpTimeout)

    defaultRequest {
        timeout {
            requestTimeoutMillis = 20000
            socketTimeoutMillis = 20000
        }
    }
}

private class SimpleLogger : Logger {
    override fun log(message: String) {
        println("[${LocalDateTime.now()}] [HttpClient] $message")
    }
}