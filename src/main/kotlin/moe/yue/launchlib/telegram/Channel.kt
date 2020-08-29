package moe.yue.launchlib.telegram

import kotlinx.coroutines.runBlocking
import moe.yue.launchlib.config
import moe.yue.launchlib.database.H2Launch
import moe.yue.launchlib.database.H2Message
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
        replyToMessageId: Long? = null
    ) = runBlocking {
            telegram.sendPhoto(
                config.telegramChannelId,
                photoUrl ?: "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/200px-No_image_available.svg.png",
                text,
                disableNotification = disableNotification,
                replyToMessageId = replyToMessageId
            )
    }

    fun newLaunch(launchLibLaunch: H2Launch, previousLaunchMessages: List<H2Message>? = null) {
        logger().info { "New launch: ${launchLibLaunch.name}" }
        val newMessage = postMessage(launchLibLaunch.detailedText(isChannel = true), launchLibLaunch.imageUrl)
            ?.also { h2.telegram.addMessage(it, "launch", launchLibLaunch.uuid) }
        // Redirect all previous launches to the newest one
        previousLaunchMessages?.forEach {
            runBlocking{telegram.editMessageCaption(it.chatId,it.messageId,"*${launchLibLaunch.name}*\nLatest status at "+
                    "https://t.me/c/${
                        newMessage?.chat?.id.toString().removePrefix("-100")
                    }/${newMessage?.messageId}")}
        }
    }

    fun listLaunches(launchLibLaunches: List<H2Launch>) {
        logger().info { "Listing launches" }
        postMessage(launchLibLaunches.listLaunchesText(isChannel = true))
            ?.also { h2.telegram.addMessage(it, "listLaunches") }

        // Delete previous listLaunches messages
        val lastListLaunches = h2.telegram.getMessages("listLaunches")
        lastListLaunches.dropLast(1).forEach {
            runBlocking {
                telegram.deleteMessage(it.chatId, it.messageId)
                telegram.sendMessage(
                    config.telegramAdminId,
                    "*Previous listLaunches message deleted:* https://t.me/c/${
                        it.chatId.toString().removePrefix("-100")
                    }/${it.messageId}".toHTML()
                )
                logger().info {
                    "Previous listLaunches message deleted: https://t.me/c/${
                        it.chatId.toString().removePrefix("-100")
                    }/${it.messageId}"
                }
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
                                "*$k*: ${timeUtils.toTime(before)} *->* ${timeUtils.toTime(after)}\n"
                            } else
                                "*$k*: ${v.first} *->* ${v.second}\n"
                        }
                        result
                    }
                }".toHTML())

                // Update launch
                h2.telegram.getMessages("launch", uuid).lastOrNull()?.let { launchMessage ->
                    // Edit sent launch messages
                        telegram.editMessageCaption(
                            launchMessage.chatId, launchMessage.messageId,
                            launch.detailedText(launchMessage.messageEpochTime, true)
                        )?.also { h2.telegram.addMessage(it, "launch") }

                    // Announce the changes
                    val timeChanges = changes["netEpochTime"]?.let {
                        val before = it.first?.toString()?.toLongOrNull()?.run { timeUtils.toShortTime(this) }
                        val after = it.second?.toString()?.toLongOrNull()?.run { timeUtils.toShortTime(this) }
                        "*Launch time changed:* $before *->* $after\n"
                    } ?: ""
                    val statusChanges = changes["statusDescription"]?.let {
                        val before = it.first?.toString()
                        val after = it.second?.toString()
                        "*Status changed:* $before *->* $after\n"
                    } ?: ""
                    if ((timeChanges + statusChanges).isNotEmpty())
                        telegram.sendMessage(
                            launchMessage.chatId,
                            (timeChanges + statusChanges).toHTML(),
                            replyToMessageId = launchMessage.messageId,
                        )?.also { h2.telegram.addMessage(it, "update", launchMessage.launchUUID) }
                            ?.also {
                                telegram.sendMessage(
                                    config.telegramAdminId,
                                    "*launch message updated:* https://t.me/c/${
                                        launchMessage.chatId.toString().removePrefix("-100")
                                    }/${launchMessage.messageId}"
                                        .toHTML()
                                )
                                logger().info {
                                    "launch message updated: https://t.me/c/${
                                        launchMessage.chatId.toString().removePrefix("-100")
                                    }/${launchMessage.messageId}"
                                }
                            }
                }

                // Edit listLaunches
                h2.telegram.getMessages("listLaunches").lastOrNull()?.let { lastListLaunches ->
                    val nextLaunches = h2.launchLib.getRecentLaunches(0, timeUtils.daysToSeconds(60)).run {
                        if (this.size <= listLaunchesLimit) this else this.take(listLaunchesLimit)
                    }
                    telegram.editMessageText(
                        lastListLaunches.chatId, lastListLaunches.messageId,
                        nextLaunches.listLaunchesText(lastListLaunches.messageEpochTime, true),
                        disableWebPagePreview = true
                    )
                    telegram.sendMessage(
                        config.telegramAdminId,
                        "*listLaunches message updated:* https://t.me/c/${
                            lastListLaunches.chatId.toString().removePrefix("-100")
                        }/${lastListLaunches.messageId}"
                            .toHTML()
                    )
                    logger().info {
                        "listLaunches message updated: https://t.me/c/${
                            lastListLaunches.chatId.toString().removePrefix("-100")
                        }/${lastListLaunches.messageId}"
                    }
                }
            }
        }
    }
}


private fun logger() = KotlinLogging.logger("[${timeUtils.toTime(timeUtils.now())}] Telegram Channel")