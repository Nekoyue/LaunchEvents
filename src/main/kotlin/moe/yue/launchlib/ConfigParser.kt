package moe.yue.launchlib

import com.typesafe.config.ConfigFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.hocon.Hocon
import java.io.File

@Serializable
data class Config(
    @SerialName("Config.telegram-token") val telegramToken: String,
    @SerialName("Config.telegram-channel-id") val telegramChannelId: Long,
    @SerialName("Config.telegram-admin-id") val telegramAdminId: Long
)

lateinit var config: Config

@OptIn(ExperimentalSerializationApi::class)
fun loadConfig() {
    val configText: String =
        File("config.conf").takeIf { it.canRead() }?.readText()
            ?: File("config.conf").apply { extractConfig() }.readText()
    config = Hocon { useConfigNamingConvention = false }.decodeFromConfig(
        Config.serializer(),
        ConfigFactory.parseString(configText)
    )
}

// Extract config file from Jar if not exist
private fun extractConfig() {
    val rawConfig = Thread.currentThread().contextClassLoader.getResource("config.conf")!!.readBytes()
    File("config.conf").writeBytes(rawConfig)
}