package moe.yue.launchlib.telegram

import kotlinx.coroutines.runBlocking
import moe.yue.launchlib.config
import moe.yue.launchlib.database.H2Launch
import moe.yue.launchlib.database.h2
import moe.yue.launchlib.launchlib.listLaunchesLimit
import moe.yue.launchlib.telegram.api.telegram
import moe.yue.launchlib.timeUtils
import mu.KotlinLogging


val telegramChannel = TelegramChannel()

class TelegramChannel {
    private fun postMessage(
        text: String,
        photoUrl: String? = null,
        disableNotification: Boolean? = null, // default false
        disableWebPagePreview: Boolean? = true,
        replyToMessageId: Long? = null
    ) = runBlocking {
        if (photoUrl == null)
            telegram.sendMessage(
                config.telegramChannelId,
                text,
                disableNotification = disableNotification,
                disableWebPagePreview = disableWebPagePreview,
                replyToMessageId = replyToMessageId
            )
        else
            telegram.sendPhoto(
                config.telegramChannelId,
                photoUrl,
                text,
                disableNotification = disableNotification,
                replyToMessageId = replyToMessageId
            )
    }

    fun newLaunch(launchLibLaunch: H2Launch) {
        logger().info { "New launch: ${launchLibLaunch.name}" }
        postMessage(launchLibLaunch.detailText(), launchLibLaunch.imageUrl)
            ?.also { h2.telegram.addMessage(it, "launch", launchLibLaunch.uuid) }
    }

    fun listLaunches(launchLibLaunches: List<H2Launch>) {
        logger().info { "Listing launches" }
        postMessage(launchLibLaunches.listLaunchesText())
            ?.also { h2.telegram.addMessage(it, "listLaunches") }

        // Delete previous listLaunches messages
        val lastListLaunches = h2.telegram.getMessages("listLaunches")
        lastListLaunches.dropLast(1).forEach {
            runBlocking {
                telegram.deleteMessage(it.chatId, it.messageId)
                telegram.sendMessage(
                    config.telegramAdminId,
                    "*Previous listLaunches message deleted:* https://t.me/c/${it.chatId}/${it.messageId}".toHTML()
                )
                logger().info { "Previous listLaunches message deleted: https://t.me/c/${it.chatId}/${it.messageId}" }
            }
            h2.telegram.deleteMessage(it.index)
        }
    }

    fun updateLaunch(uuid: String, changes: MutableMap<String, Pair<Any?, Any?>>) {
        h2.launchLib.getLaunch(uuid)?.let { launch ->
            logger().info { "Update launch: ${launch.name}" }
            runBlocking {
                // Notify admin
                telegram.sendMessage(config.telegramAdminId, "*${launch.name} updates:*\n${
                    changes.let {
                        var result = ""
                        it.forEach { (k, v) ->
                            result += if (k.toLowerCase().contains("epoch") && v.first?.toString()
                                    ?.toLongOrNull() != null && v.second?.toString()?.toLongOrNull() != null
                            ) { // Convert epoch time to readable time
                                val before = v.first?.toString()?.toLongOrNull()!!
                                val after = v.second?.toString()?.toLongOrNull()!!
                                "*$k*: ${timeUtils.toTime(before)} -> ${timeUtils.toTime(after)}\n"
                            } else
                                "*$k*: ${v.first} -> ${v.second}\n"
                        }
                        result
                    }
                }".toHTML())

                // Update launch
                h2.telegram.getMessages("launch", uuid).lastOrNull()?.let { lastLaunch ->
                    // Edit sent launch messages
                    if (launch.imageUrl == null)
                        telegram.editMessageText(
                            lastLaunch.chatId, lastLaunch.messageId,
                            launch.detailText(lastLaunch.messageEpochTime), disableWebPagePreview = true
                        )?.also { h2.telegram.addMessage(it, "launch") }
                    else
                        telegram.editMessageCaption(
                            lastLaunch.chatId, lastLaunch.messageId,
                            launch.detailText(lastLaunch.messageEpochTime)
                        )?.also { h2.telegram.addMessage(it, "launch") }

                    // Announce the changes
                    val timeChanges = changes["netEpochTime"]?.let {
                        val before = it.first?.toString()?.toLongOrNull()
                        val after = it.second?.toString()?.toLongOrNull()
                        "Launch time changed: $before -> $after\n"
                    } ?: ""
                    val statusChanges = changes["statusDescription"]?.let {
                        val before = it.first?.toString()?.toLongOrNull()
                        val after = it.second?.toString()?.toLongOrNull()
                        "Status changed: $before -> $after\n"
                    } ?: ""
                    if ((timeChanges + statusChanges).isNotEmpty())
                        telegram.sendMessage(
                            lastLaunch.chatId,
                            timeChanges + statusChanges,
                            replyToMessageId = lastLaunch.messageId,
                        )?.also { h2.telegram.addMessage(it, "update") }
                            ?.also {
                                telegram.sendMessage(
                                    config.telegramAdminId,
                                    "*launch message updated:* https://t.me/c/${lastLaunch.chatId}/${lastLaunch.messageId}"
                                        .toHTML()
                                )
                                logger().info { "launch message updated: https://t.me/c/${lastLaunch.chatId}/${lastLaunch.messageId}" }
                            }
                }

                // Update listLaunches
                h2.telegram.getMessages("listLaunches").lastOrNull()?.let { lastListLaunches ->
                    val nextLaunches = h2.launchLib.getRecentLaunches(0, timeUtils.daysToSeconds(60)).run {
                        if (this.size <= listLaunchesLimit) this else this.take(listLaunchesLimit)
                    }
                    telegram.editMessageText(
                        lastListLaunches.chatId, lastListLaunches.messageId,
                        nextLaunches.listLaunchesText(lastListLaunches.messageEpochTime), disableWebPagePreview = true
                    )
                    telegram.sendMessage(
                        config.telegramAdminId,
                        "*listLaunches message updated:* https://t.me/c/${lastListLaunches.chatId}/${lastListLaunches.messageId}"
                            .toHTML()
                    )
                    logger().info { "listLaunches message updated: https://t.me/c/${lastListLaunches.chatId}/${lastListLaunches.messageId}" }
                }
            }
        }
    }
}


private fun logger() = KotlinLogging.logger("[${timeUtils.toTime(timeUtils.now())}] Telegram Channel")