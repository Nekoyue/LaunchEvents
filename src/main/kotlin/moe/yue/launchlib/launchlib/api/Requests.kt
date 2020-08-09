package moe.yue.launchlib.launchlib.api

import io.ktor.client.request.*
import moe.yue.launchlib.Http
import moe.yue.launchlib.httpClient
import moe.yue.launchlib.timeUtils

val launchLib = LaunchLib()

class LaunchLib : Http() {
    private val apiUrl = "https://ll.thespacedevs.com/2.0.0/launch"

    suspend fun get(
        after: Long = timeUtils.getNow() - timeUtils.daysToSeconds(5),
        before: Long = after + timeUtils.daysToSeconds(360),
        limit: Int = 100
    ): List<LaunchLibLaunch> =
        httpClient.get<LaunchLibResult>(apiUrl) {
            body = addParameters(
                "net__gte" to timeUtils.toSimpleDate(after),
                "net__lte" to timeUtils.toSimpleDate(before),
                "limit" to limit
            )
        }.results
//    suspend fun getUpcoming(): List<LaunchLibLaunch> = httpClient.get<LaunchLibResult>("$apiUrl/upcoming/?limit=100").results
//    suspend fun getPrevious(): List<LaunchLibLaunch> = httpClient.get<LaunchLibResult>("$apiUrl/previous/?limit=100").results
}