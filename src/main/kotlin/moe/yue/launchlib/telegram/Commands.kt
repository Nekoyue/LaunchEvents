package moe.yue.launchlib.telegram

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import moe.yue.launchlib.When
import moe.yue.launchlib.database.h2
import moe.yue.launchlib.telegram.api.TelegramMessage
import moe.yue.launchlib.telegram.api.botUsername
import moe.yue.launchlib.telegram.api.telegram
import moe.yue.launchlib.timeUtils
import mu.KotlinLogging
import java.time.Instant
import java.time.ZoneId
import kotlin.system.exitProcess

private fun command(command: String) = object : WhenTelegramMessage {
    override fun function(value: TelegramMessage): Boolean = value.text?.let {
        (it == "/$command" || it.startsWith("/$command ") ||
                it == "/$command@$botUsername" || it.startsWith("/$command@$botUsername "))
    } ?: false
}

// Parameter should be the form of either "/command *parameter*" or "/start *parameter*_value"
private fun command(command: String, parameter: String) = object : WhenTelegramMessage {
    override fun function(value: TelegramMessage): Boolean =
        command(command).function(value) &&
                parameter == value.text?.let { getParameter(it) }
}

// Get the second word in a command e.g. "/command *parameter*" or "/start *parameter*_value"
private fun TelegramMessage.getParameter() = this.text?.let { getParameter(it) }
private fun getParameter(text: String) = text.substringAfter(" ", "").ifEmpty { null }?.substringBefore("_")

// Get the value of a start command e.g. "/start parameter_*value*"
private fun TelegramMessage.getValue() = this.text?.let { getValue(it) }
private fun getValue(text: String) = text.substringAfter("_", "").ifEmpty { null }

private interface WhenTelegramMessage : When<TelegramMessage>

suspend fun processMessages(telegramMessage: TelegramMessage) {
    logger.info {
        "Received message ${telegramMessage.text} ${
            telegramMessage.from?.let { "from ${it.firstName}${" " add it.lastName} (${"@" add it.username add " | "}${it.id})" }
        }"
    }
    when (val it = telegramMessage) {
        in command("start", "time") -> {
            val launch = it.getValue()?.run { h2.launchLib.getLaunch(this) }
            val epochTime = launch?.netEpochTime
            val text = epochTime?.let { epochTime ->
                // Convert to local time given a list of time zones and the epochTime
                // e.g. "America/Los_Angeles" -> "Los Angeles (UTC-8): Aug 17, 03:05:30"
                fun List<String>.convert(): String {
                    var result = ""
                    this.forEach { zoneName ->
                        result += "UTC${
                            ZoneId.of(zoneName).rules.getOffset(Instant.ofEpochSecond(epochTime)).toString()
                                // Shorten e.g. "+08:00" -> "+8"
                                .removeSuffix(":00").replace("-0", "-").replace("+0", "+")
                        }: ${
                            timeUtils.toFullTime(epochTime, zoneName).split(" ")
                                .run { "${this[0]} ${this[1]} ${this[3]}" }
                        } (*${
                            // Shorten e.g. "America/Los_Angeles" -> "Los Angeles"
                            zoneName.replace("_", " ").substringAfter("/")
                        }*)\n"
                    }
                    return result
                }
                ("*Time Zone Converter* for\n" +
                        "*${launch.name}*\n\n" +
                        listOf(
                            "America/Los_Angeles",
                            "America/New_York",
                            "America/Sao_Paulo"
                        ).convert() +
                        "*UTC+0: ${
                            timeUtils.toFullTime(epochTime).split(" ").run { "${this[0]} ${this[1]} ${this[3]}" }
                        } (Greenwich)*\n" +
                        listOf(
                            "Europe/London",
                            "Europe/Paris",
                            "Europe/Moscow",
                            "Asia/Singapore",
                            "Asia/Tokyo",
                            "Australia/Sydney",
                            "Pacific/Auckland"
                        ).convert() +
                        "\n[Other Time Zones](https://www.thetimezoneconverter.com/?t=${
                            timeUtils.toTime(epochTime).substringAfter(" ")
                                .substringBeforeLast(":") // convert to e.g. 12:34
                        }&tz=UTC)")
                    .toHTML()
            } ?: "Invalid request.".also { logger.debug { "Invalid request: /start time" } }
            telegram.sendMessage(it.chat.id, text, disableWebPagePreview = true)
        }
        in command("start", "location") -> {
            val launch = it.getValue()?.run { h2.launchLib.getLaunch(this) }
            val latitude = launch?.padLatitude?.toDoubleOrNull()
            val longitude = launch?.padLongitude?.toDoubleOrNull()
            if (latitude != null && longitude != null) {
                telegram.sendMessage(
                    it.chat.id,
                    "*${launch.name}*'s launch site is located at\n*${launch.padLocationName}*".toHTML()
                )
                coroutineScope {
                    launch {
                        delay(500)
                        telegram.sendLocation(it.chat.id, latitude, longitude)
                    }
                }
            } else {
                telegram.sendMessage(it.chat.id, "Invalid request.")
                    .also { logger.debug { "Invalid request: /start location" } }
            }
        }
        in command("listlaunches"), in command("list"), in command("ll") -> {
            val limit = 5
            val text = h2.launchLib.getRecentLaunches(0, timeUtils.daysToSeconds(60)).run {
                if (this.size <= limit) this else this.take(limit)
            }.text()
            telegram.sendMessage(it.chat.id, text)
        }
        in command("nextlaunch"), in command("next"), in command("nl") -> {
            val launch = h2.launchLib.getRecentLaunches(0, timeUtils.daysToSeconds(60)).firstOrNull()
            if (launch != null) {
                if (launch.imageUrl != null) telegram.sendPhoto(it.chat.id, launch.imageUrl, launch.text())
                else telegram.sendMessage(it.chat.id, launch.text())
            } else telegram.sendMessage(it.chat.id, "No launch data available in the next 60 days.")
        }
        in command("debug", "stop") -> {
            exitProcess(-1)
        }
        in command("debug", "exception") -> {
            throw AssertionError("Received /debug exception")
        }
        in command("help") -> {
            telegram.sendMessage(
                it.chat.id, """
                Help Menu:
            """.trimIndent()
            )
        }

    }
}

private val logger = KotlinLogging.logger("[${timeUtils.toTime(timeUtils.now())}] Telegram Commands")