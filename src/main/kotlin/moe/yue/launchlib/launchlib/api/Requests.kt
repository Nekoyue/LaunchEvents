package moe.yue.launchlib.launchlib.api

import io.ktor.client.request.*
import moe.yue.launchlib.httpClient

val launchLib = LaunchLib()

class LaunchLib {
    private val apiUrl = "https://ll.thespacedevs.com/2.0.0/launch"

    suspend fun getUpcoming(): List<Launch> = httpClient.get<Result>("$apiUrl/upcoming/?limit=100").results
    suspend fun getPrevious(): List<Launch> = httpClient.get<Result>("$apiUrl/previous/?limit=100").results
}