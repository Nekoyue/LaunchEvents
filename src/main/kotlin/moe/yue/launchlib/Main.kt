package moe.yue.launchlib

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import moe.yue.launchlib.launchlib.scheduler
import moe.yue.launchlib.telegram.api.telegram
import moe.yue.launchlib.telegram.api.updateDispatcher
import mu.KotlinLogging

fun main() {
    loadConfig()
    var failures = 0
    while (failures < 10)
        runBlocking {
            try {
                coroutineScope {
                    delay(5000 * failures * failures.toLong())
                    launch {
                        updateDispatcher() // Handle commands received by the bot
                    }
                    launch {
                        scheduler() // Handle regular Launch Library updates and channel posting
                    }
                    launch {
                        while (failures > 0) {
                            delay(30000)
                            failures--
                        }
                    }
                }
            } catch (exception: Exception) {
                logger.error(exception){"Exception: $exception"}
                try {
                    telegram.sendMessage(config.telegramAdminId, "Exception:\n $exception")
                } catch (_: Exception) {
                }
                failures++
            }
        }
}


private val logger = KotlinLogging.logger("[${timeUtils.toTime(timeUtils.now)}] Main")