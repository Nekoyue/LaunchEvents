package moe.yue.launchlib

import kotlinx.coroutines.*
import moe.yue.launchlib.launchlib.scheduler
import moe.yue.launchlib.telegram.api.telegram
import moe.yue.launchlib.telegram.api.updateDispatcher
import mu.KotlinLogging

fun main() {
    loadConfig()
    var failures = 0
    while (failures <= 15) {
        runBlocking {
            try {
                supervisorScope {
                    val handler = CoroutineExceptionHandler { coroutineContext, exception ->
                        failures++
                        logger.error { "Failure count: $failures, process will restart after ${5 * failures * failures} seconds." }
                        logger.error { "Exception: $exception" }
                        GlobalScope.launch { // Send exception message to admin via Telegram.
                            try {
                                withTimeout(5000) {
                                    telegram.sendMessage(
                                        config.telegramAdminId, "Exception:\n$exception\n\n" +
                                                "Failure count: $failures, process will restart after ${5 * failures * failures} seconds."
                                    )
                                }
                            } catch (exception: Exception) {
                            }
                        }
                        this.cancel()
                    }
                    delay(5000 * failures * failures.toLong())
                    launch(handler) {
                        updateDispatcher() // Handle commands received by the bot
                    }
                    launch(handler) {
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
            }
        }
    }
}


private val logger = KotlinLogging.logger("[${timeUtils.toTime(timeUtils.now())}] Main")