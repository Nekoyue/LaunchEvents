package moe.yue.launchlib.telegram.api

import moe.yue.launchlib.telegram.processMessages
import moe.yue.launchlib.timeUtils
import java.lang.Long.max
import kotlin.math.absoluteValue

lateinit var botUsername: String

suspend fun updateDispatcher() {
    var offset = 0L
    botUsername = telegram.getMe()!!.username!!
    while (true) {
        telegram.getUpdates(offset = offset).also { if (it.isNullOrEmpty()) offset = 0L }
            ?.filter { // Only keep the messages sent within last 2 minutes
                it.message?.epochTime?.minus(timeUtils.now)?.let { differences -> differences.absoluteValue < 120 }
                    ?: true
            }
            ?.forEach {
                it.message?.handler()
                offset = max(offset, it.updateId + 1)
            }
    }
}


private suspend fun TelegramMessage.handler() {
    // Temporary approach, to be improved
    processMessages(this)
}