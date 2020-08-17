package moe.yue.launchlib.telegram

import kotlinx.coroutines.runBlocking
import moe.yue.launchlib.config
import moe.yue.launchlib.database.H2Launch
import moe.yue.launchlib.database.h2
import moe.yue.launchlib.telegram.api.telegram
import moe.yue.launchlib.timeUtils
import mu.KotlinLogging


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

    fun updateLaunch(uuid: String, changes: MutableMap<String, Pair<Any?, Any?>>) {
        logger.info("Update launch: ${h2.launchLib.getLaunch(uuid)?.name}")
        postMessage(changes.toString())?.also { h2.telegram.addMessage(it, "update", uuid) }
    }

    fun newLaunch(launchLibLaunch: H2Launch) {
        logger.info("New launch: ${launchLibLaunch.name}")
        postMessage(launchLibLaunch.text())?.also { h2.telegram.addMessage(it, "launch", launchLibLaunch.uuid) }
    }

    fun listLaunches(launchLibLaunches: List<H2Launch>) {
        logger.info("List launches")
        postMessage(launchLibLaunches.text())?.also { h2.telegram.addMessage(it, "listLaunches") }
    }
}


private val logger = KotlinLogging.logger("[${timeUtils.toTime(timeUtils.getNow())}] Telegram Channel")