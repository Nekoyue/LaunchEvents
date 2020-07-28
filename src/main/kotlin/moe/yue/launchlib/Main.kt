package moe.yue.launchlib

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import moe.yue.launchlib.telegram.api.telegram
import moe.yue.launchlib.telegram.api.updateDispatcher

val exceptionHandler = CoroutineExceptionHandler { _, exception ->
    runBlocking {
        telegram.sendMessage(config.telegramAdminId, "Exception:\n $exception")
    }
    throw exception
}

fun main() {
    loadConfig()
    while (true)
        runBlocking {
            launch(exceptionHandler) {
                updateDispatcher()
            }
            delay(1000)
        }
}


