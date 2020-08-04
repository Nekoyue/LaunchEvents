package moe.yue.launchlib

import kotlinx.coroutines.runBlocking
import moe.yue.launchlib.database.h2

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import moe.yue.launchlib.launchlib.scheduler
import moe.yue.launchlib.telegram.api.telegram
import moe.yue.launchlib.telegram.api.updateDispatcher

fun main() {
    loadConfig()
    var failures = 0
    while (failures < 10)
        runBlocking {
            try {
                coroutineScope {
                    delay(5000 * failures * failures.toLong())
                    launch {
                        updateDispatcher()
                    }
                    launch {
                        scheduler()
                    }
                    launch {
                        while (failures > 0) {
                            delay(30000)
                            failures--
                        }
                    }
                }
            } catch (exception: Exception) {
                try {
                    telegram.sendMessage(config.telegramAdminId, "Exception:\n $exception")
                } catch (_: Exception) {
                }
                failures++
            }
        }
}
