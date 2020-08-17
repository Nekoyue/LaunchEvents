package moe.yue.launchlib

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
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
        level = LogLevel.NONE
    }
    install(HttpTimeout)

    defaultRequest {
        timeout {
            requestTimeoutMillis = 20000
            socketTimeoutMillis = 20000
        }
    }
}

open class Http{
    // Add non-null parameters to the request form. Pair<parameter name, value>
    internal fun <T> addParameters(vararg parameters: Pair<String, T?>) =
        FormDataContent(Parameters.build {
            parameters.forEach { pair ->
                pair.second?.let { append(pair.first, pair.second.toString()) }
            }
        })
}

private class SimpleLogger : Logger {
    override fun log(message: String) {
        println("[${LocalDateTime.now()}] [HttpClient] $message")
    }
}