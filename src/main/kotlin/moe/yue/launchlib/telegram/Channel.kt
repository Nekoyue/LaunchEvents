package moe.yue.launchlib.telegram

import kotlinx.coroutines.runBlocking
import moe.yue.launchlib.config
import moe.yue.launchlib.database.H2Launch
import moe.yue.launchlib.telegram.api.telegram


val telegramChannel = TelegramChannel()

class TelegramChannel {
    private fun postMessage(
        text: String,
        disableNotification: Boolean? = null, // default false
        replyToMessageId: Long? = null
    ) = runBlocking {
        telegram.sendMessage(
            config.telegramChannelId,
            text,
            disableNotification = disableNotification,
            replyToMessageId = replyToMessageId
        )
    }

    fun updateLaunch(changes: MutableMap<String, Pair<Any?, Any?>>) {

    }

    fun newLaunch(launchLibLaunch: H2Launch) {

    }

    fun listLaunches(launchLibLaunches: List<H2Launch>) {

    }
}