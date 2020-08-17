package moe.yue.launchlib.telegram

import moe.yue.launchlib.launchlib.api.launchLib
import moe.yue.launchlib.telegram.api.TelegramMessage
import moe.yue.launchlib.telegram.api.telegram
import moe.yue.launchlib.timeUtils
import mu.KotlinLogging


fun getThis(telegramMessage: TelegramMessage): String {
    var result = "*Basic information of this chat:*\n"
    telegramMessage.from?.let {
        result += "User: `${it.firstName}${it.lastName add " "}` " +
                "(${"@" add it.username add " | "}`${it.id}`)\n"
    }
    result += "Chat: `${telegramMessage.chat.id}` (${telegramMessage.chat.type})\n"
    result += "Message Id: `${telegramMessage.messageId}`\n"
    result += "Date: ${telegramMessage.epochTime.run { timeUtils.toTime(this) }}"
    return result.toHTML()
}

suspend fun processMessages(telegramMessage: TelegramMessage) {
    logger.info("Received message ${telegramMessage.text} ${
        telegramMessage.from?.let { "from ${it.firstName}${" " add it.lastName} (${"@" add it.username add " | "}${it.id})" }
    }")
    when {
        telegramMessage.text?.startsWith("/getThis") == true -> {
            telegram.sendMessage(telegramMessage.chat.id, getThis(telegramMessage))
        }
        telegramMessage.text?.startsWith("/getu") == true -> {
            println(launchLib.get())
        }
        telegramMessage.text?.startsWith("/getp") == true -> {
            println(launchLib.get())
        }
        else -> {
            val a = """
                *bold \*text*
                _italic \*text_
                __underline__
                ~strikethrough~
                *bold _italic bold ~italic bold strikethrough~ __underline italic bold___ bold*
                [inline URL](http://www.example.com/)
                [inline mention of a user](tg://user?id=123456789)
                `inline fixed-width code`
                ```
                pre-formatted fixed-width code block
                ```
                ```python
                pre-formatted fixed-width code block written in the Python programming language
                ```
            """.trimIndent()
            telegram.sendMessage(telegramMessage.chat.id, a.toHTML())
        }
    }
}

private val logger = KotlinLogging.logger("[${timeUtils.toTime(timeUtils.getNow())}] Telegram Commands")