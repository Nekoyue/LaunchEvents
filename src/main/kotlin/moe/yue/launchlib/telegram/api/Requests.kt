package moe.yue.launchlib.telegram.api

import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import moe.yue.launchlib.config
import moe.yue.launchlib.httpClient

val telegram = Requests(config.telegramToken)

// ONLY contains basic api methods used by the project.
class Requests(token: String) {
    private val apiUrl = "https://api.telegram.org/"
    private val baseUrl = "${apiUrl}bot${token}/"

    private suspend inline fun <reified T> post(methodName: String, parameters: FormDataContent) =
        httpClient.post<Result<T>>("$baseUrl$methodName") {
            body = parameters
        }.result

    // Add non-null parameters to the request form. Pair<parameter name, value>
    private fun <T> addParameters(vararg parameters: Pair<String, T?>) =
        FormDataContent(Parameters.build {
            parameters.forEach { pair ->
                pair.second?.let { append(pair.first, pair.second.toString()) }
            }
        })


    suspend fun getUpdates(
        offset: Long? = null,
        limit: Int? = null, // 1..100, default 100
        timeout: Int? = 15 // Also see requestTimeoutMillis and socketTimeoutMillis in httpClient@Main.kt
    ) = post<List<Update>>(
        "getUpdates",
        addParameters(
            "offset" to offset,
            "limit" to limit,
            "timeout" to timeout
        )
    )

    suspend fun sendMessage(
        chatId: Long,
        text: String,
        parseMode: String? = "HTML", // MarkdownV2, Markdown, or HTML
        disableWebPagePreview: Boolean? = null, // default false
        disableNotification: Boolean? = null, // default false
        replyToMessageId: Long? = null
    ) = post<Message>(
        "sendMessage",
        addParameters(
            "chat_id" to chatId,
            "text" to text,
            "parse_mode" to parseMode,
            "disable_web_page_preview" to disableWebPagePreview,
            "disable_notification" to disableNotification,
            "reply_to_message_id" to replyToMessageId
        )
    )

    suspend fun editMessageText(
        chatId: Long,
        messageId: Long,
        text: String,
        parseMode: String? = "HTML", // either MarkdownV2 or HTML
        disableWebPagePreview: Boolean? = null // default false
    ) = post<Message>(
        "editMessageText",
        addParameters(
            "chat_id" to chatId,
            "message_id" to messageId,
            "text" to text,
            "parse_mode" to parseMode,
            "disable_web_page_preview" to disableWebPagePreview
        )
    )


    suspend fun sendPhoto(
        chatId: Long,
        photoUrl: String,
        caption: String? = null,
        parseMode: String? = "HTML", // either MarkdownV2 or HTML
        disableNotification: Boolean? = null, // default false
        replyToMessageId: Long? = null
    ) = post<Message>(
        "sendPhoto",
        addParameters(
            "chat_id" to chatId,
            "photo" to photoUrl,
            "caption" to caption,
            "parse_mode" to parseMode,
            "disable_notification" to disableNotification,
            "reply_to_message_id" to replyToMessageId
        )
    )

    suspend fun editMessageCaption(
        chatId: Long,
        messageId: Long,
        caption: String? = null,
        parseMode: String? = "HTML", // either MarkdownV2 or HTML
    ) = post<Message>(
        "editMessageCaption",
        addParameters(
            "chat_id" to chatId,
            "message_id" to messageId,
            "caption" to caption,
            "parse_mode" to parseMode
        )
    )

    suspend fun deleteMessage(
        chatId: Long,
        messageId: Long
    ) = post<Boolean>(
        "deleteMessage",
        addParameters(
            "chat_id" to chatId,
            "message_id" to messageId,
        )
    )

    suspend fun forwardMessage(
        chatId: Long,
        fromChatId: Long,
        messageId: Long,
        disableNotification: Boolean? = null // default false
    ) = post<Message>(
        "forwardMessage",
        addParameters(
            "chat_id" to chatId,
            "from_chat_id" to fromChatId,
            "message_id" to messageId,
            "disable_notification" to disableNotification
        )
    )
}