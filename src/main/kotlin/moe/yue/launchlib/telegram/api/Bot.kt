package moe.yue.launchlib.telegram.api

import moe.yue.launchlib.telegram.processMessages
import java.lang.Long.max

suspend fun updateDispatcher() {
    var offset = 0L
    while (true) {
        telegram.getUpdates(offset = offset).also { if (it.isNullOrEmpty()) offset = 0L }?.forEach {
            it.message?.handler()
            offset = max(offset, it.updateId + 1)
        }
    }
}


private suspend fun TelegramMessage.handler() {
    // Temporary approach, to be improved.
    processMessages(this)
}