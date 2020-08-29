package moe.yue.launchlib.telegram

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import moe.yue.launchlib.When
import moe.yue.launchlib.config
import moe.yue.launchlib.database.h2
import moe.yue.launchlib.launchlib.newListLaunches
import moe.yue.launchlib.telegram.api.TelegramMessage
import moe.yue.launchlib.telegram.api.botUsername
import moe.yue.launchlib.telegram.api.telegram
import moe.yue.launchlib.timeUtils
import mu.KotlinLogging
import kotlin.system.exitProcess

private fun command(command: String) = object : WhenTelegramMessage {
    override fun function(value: TelegramMessage): Boolean = value.text?.let {
        (it == "/$command" || it.startsWith("/$command ") ||
                it == "/$command@$botUsername" || it.startsWith("/$command@$botUsername "))
    } ?: false
}

// Parameter is defined as either "/command *parameter*" or "/start *parameter*_value"
private fun command(command: String, parameter: String?) = object : WhenTelegramMessage {
    override fun function(value: TelegramMessage): Boolean =
        command(command).function(value) &&
                parameter == value.text?.let { getParameter(it) }
}

// Get the second word of a command e.g. "/command *parameter*", "/command *parameter* information", or "/start *parameter*_value"
private fun TelegramMessage.getParameter() = this.text?.let { getParameter(it) }
private fun getParameter(text: String) = text.substringAfter(" ", "").ifEmpty { null }?.substringBefore("_")

// Get the value within a "/start" command e.g. "/start parameter_*value*"
private fun TelegramMessage.getValue() = this.text?.let { getValue(it) }
private fun getValue(text: String) = text.substringAfter("_", "").ifEmpty { null }

private interface WhenTelegramMessage : When<TelegramMessage>

suspend fun processMessages(telegramMessage: TelegramMessage) {
    logger().info {
        "Received message ${telegramMessage.text} ${
            telegramMessage.from?.let { "from ${it.firstName}${" " add it.lastName} (${"@" add it.username add " | "}${it.id})" }
        }"
    }
    when (val it = telegramMessage) {
        in command("start", "time") -> {
            val text = it.getValue()?.run { h2.launchLib.getLaunch(this) }
                ?.let { launch ->
                    if (launch.imageUrl == null)
                        telegram.sendMessage(it.chat.id, launch.detailedText(), disableWebPagePreview = true)
                    else
                        telegram.sendPhoto(it.chat.id, launch.imageUrl, launch.detailedText())

                    launch.timeZoneConverterText()
                } ?: "Invalid request."
                .also { logger().debug { "Invalid request: /start time" } }

            coroutineScope {
                launch {
                    delay(500)
                    telegram.sendMessage(it.chat.id, text, disableWebPagePreview = true)
                }
            }
        }

        in command("start", "location") -> {
            val launch = it.getValue()?.run { h2.launchLib.getLaunch(this) }
            val latitude = launch?.padLatitude?.toDoubleOrNull()
            val longitude = launch?.padLongitude?.toDoubleOrNull()
            if (latitude != null && longitude != null) {
                telegram.sendMessage(
                    it.chat.id,
                    launch.locationText()
                )
                coroutineScope {
                    launch {
                        delay(500)
                        telegram.sendLocation(it.chat.id, latitude, longitude)
                    }
                }
            } else {
                telegram.sendMessage(it.chat.id, "Invalid request.")
                    .also { logger().debug { "Invalid request: /start location" } }
            }
        }

        in command("start", "details") -> {
            val text = it.getValue()?.run { h2.launchLib.getLaunch(this) }
                ?.let { launch ->
                    if (launch.imageUrl == null)
                        telegram.sendMessage(it.chat.id, launch.detailedText(), disableWebPagePreview = true)
                    else
                        telegram.sendPhoto(it.chat.id, launch.imageUrl, launch.detailedText())

                    "*Mission Description* for \n*${launch.name}*\n\n${launch.missionDescription}".toHTML()
                } ?: "Invalid request."
                .also { logger().debug { "Invalid request: /start details" } }

            coroutineScope {
                launch {
                    delay(500)
                    telegram.sendMessage(it.chat.id, text, disableWebPagePreview = true)
                }
            }
        }

        in command("listlaunches"), in command("list"), in command("ll") -> {
            val limit = 5
            val text = h2.launchLib.getRecentLaunches(0, timeUtils.daysToSeconds(60)).run {
                if (this.size <= limit) this else this.take(limit)
            }.listLaunchesText()
            telegram.sendMessage(it.chat.id, text, disableWebPagePreview = true)
        }

        in command("nextlaunch"), in command("next"), in command("nl") -> {
            val launch = h2.launchLib.getRecentLaunches(0, timeUtils.daysToSeconds(60)).firstOrNull()
            if (launch != null) {
                if (launch.imageUrl != null) telegram.sendPhoto(it.chat.id, launch.imageUrl, launch.detailedText())
                else telegram.sendMessage(it.chat.id, launch.detailedText(), disableWebPagePreview = true)
            } else telegram.sendMessage(it.chat.id, "No launch data available in the next 60 days.")
        }

        in command("feedback") -> {
            val text: String
            when {
                it.chat.type != "private" -> text = "Please send your suggestion via direct message."
                it.text?.substringAfter("/feedback", "")?.removePrefix("@$botUsername").isNullOrEmpty() ->
                    text = "Please attach your suggestion after /feedback."
                else -> {
                    text = "Thanks for your suggestion!"
                    telegram.forwardMessage(config.telegramAdminId, it.chat.id, it.messageId)
                }
            }
            telegram.sendMessage(it.chat.id, text, replyToMessageId = it.messageId)
        }

        in command("debug", "stop") -> {
            if (it.chat.id == config.telegramAdminId)
                exitProcess(-1)

        }
        in command("debug", "exception") -> {
            if (it.chat.id == config.telegramAdminId)
                throw AssertionError("Received /debug exception")
        }
        in command("debug", "newListLaunches") -> {
            if (it.chat.id == config.telegramAdminId)
                newListLaunches()
        }

        in command("help"), in command("start", null) -> {
            telegram.sendMessage(
                it.chat.id, """
                *Available commands:*
                /nl - Information of the next launch.
                /ll - List upcoming launches.
                /feedback - Write your feedback to the developer.
                
                *This bot uses information from Launch Library 2:*
                https://thespacedevs.com/
                Consider supporting them on [Patreon](https://www.patreon.com/bePatron?u=32219121&redirect_uri=https%3A%2F%2Ft%2Eme%2FLaunchThisBot).
                
                *Source code:*
                https://github.com/Nekoyue/LaunchEvents
            """.trimIndent().toHTML(), disableWebPagePreview = true
            )
        }

    }
}

private fun logger() = KotlinLogging.logger("[${timeUtils.toTime(timeUtils.now())}] Telegram Commands")