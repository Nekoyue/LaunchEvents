package moe.yue.launchlib

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking

val httpClient = HttpClient(OkHttp) {
    install(JsonFeature) {
        serializer = KotlinxSerializer(
            kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
                isLenient = true
            }
        )
    }
// Currently not using Ktor's internal logger
//    install(Logging) {
//        logger = SimpleLogger()
//        level = LogLevel.NONE
//    }
    install(HttpTimeout)

    defaultRequest {
        timeout {
            requestTimeoutMillis = 20000
            socketTimeoutMillis = 20000
        }
    }

    HttpResponseValidator {
        // Overrides the default exception handler
        // to provide more debug information in the exception
        class ClientRequestException(
            response: HttpResponse
        ) : ResponseException(response) {
            override val message: String? =
                "Client request(${response.call.request.url}) invalid: ${response.status}\n" +
                        "Request parameters: ${response.call.request.url.parameters}\n"+
                        "Result content: ${runBlocking { response.content.readUTF8Line(5000) }}"
        }

        validateResponse { response: HttpResponse ->
            val statusCode = response.status.value
            when (statusCode) {
                in 300..399 -> throw RedirectResponseException(response)
                in 400..499 -> throw ClientRequestException(response)
                in 500..599 -> throw ServerResponseException(response)
            }
            if (statusCode >= 600) {
                throw ResponseException(response)
            }
        }
    }
}

open class Http {
    // Add non-null parameters to the request form. Pair<parameter name, value>
    internal fun <T> addParameters(vararg parameters: Pair<String, T?>) =
        FormDataContent(Parameters.build {
            parameters.forEach { pair ->
                pair.second?.let { append(pair.first, pair.second.toString()) }
            }
        })
}


//private class SimpleLogger : Logger {
//    override fun log(message: String) {
//        println("[${LocalDateTime.now()}] [HttpClient] $message")
//    }
//}