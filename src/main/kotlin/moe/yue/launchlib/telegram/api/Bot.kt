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
            ?.forEach {
                // Only reply to messages sent in the last 60 seconds.
                if(it.message?.epochTime?.minus(timeUtils.now)?.let { differences -> differences.absoluteValue < 60 } != false)
                    it.message?.handler()
                offset = max(offset, it.updateId + 1)
            }
    }
}


private suspend fun TelegramMessage.handler() {
    // Temporary approach, to be improved
    processMessages(this)
}