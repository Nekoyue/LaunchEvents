package moe.yue.launchlib.launchlib.api

import io.ktor.client.request.*
import moe.yue.launchlib.Http
import moe.yue.launchlib.httpClient
import moe.yue.launchlib.timeUtils

val launchLib = LaunchLib()

class LaunchLib : Http() {
    private val apiUrl = "https://ll.thespacedevs.com/2.2.0/launch/"

    suspend fun get(
        after: Long = timeUtils.now() - timeUtils.daysToSeconds(5),
        before: Long = after + timeUtils.daysToSeconds(360),
        limit: Int = 50
    ): List<LaunchLibLaunch> =
        httpClient.get<LaunchLibResult>(
            "$apiUrl?net__gte=${timeUtils.toDate(after)}T00:00:00Z&net__lte=${timeUtils.toDate(before)}T00:00:00Z&limit=$limit"
        ).results
//    suspend fun getUpcoming(): List<LaunchLibLaunch> = httpClient.get<LaunchLibResult>("$apiUrl/upcoming/?limit=100").results
//    suspend fun getPrevious(): List<LaunchLibLaunch> = httpClient.get<LaunchLibResult>("$apiUrl/previous/?limit=100").results
}