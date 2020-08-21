package moe.yue.launchlib.telegram.api

import moe.yue.launchlib.telegram.add
import moe.yue.launchlib.telegram.processMessages
import moe.yue.launchlib.timeUtils
import mu.KotlinLogging
import java.lang.Long.max
import kotlin.math.absoluteValue

lateinit var botUsername: String

suspend fun updateDispatcher() {
    var offset = 0L
    botUsername = telegram.getMe()!!.username!!
    while (true) {
        telegram.getUpdates(offset = offset).also { if (it.isNullOrEmpty()) offset = 0L }
            ?.forEach {
                // Only reply to messages sent within the last 60 seconds.
                println("Received message")
                if (it.message?.epochTime?.minus(timeUtils.now())
                        ?.let { differences -> differences.absoluteValue < 60 } == true
                )
                    it.message.handler()
                else
                    KotlinLogging.logger("[${timeUtils.toTime(timeUtils.now())}] Telegram dispatcher")
                        .debug {
                            "Dropped message ${it.message?.text} ${
                                it.message?.from?.let { "from ${it.firstName}${" " add it.lastName} (${"@" add it.username add " | "}${it.id})" }
                            }"
                        }
                offset = max(offset, it.updateId + 1)
            }
    }
}


private suspend fun TelegramMessage.handler() {
    processMessages(this)
}