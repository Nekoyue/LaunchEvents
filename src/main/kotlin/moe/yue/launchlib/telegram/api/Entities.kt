package moe.yue.launchlib.telegram.api

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

// This file contains basic json objects from http request used by the project
@Serializable
data class TelegramResult<T>(
    @SerialName("ok") val ok: Boolean,
    @SerialName("error_code") val errorCode: Int? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("result") val result: T?
)

@Serializable
data class TelegramUpdate(
    @SerialName("update_id") val updateId: Long,
    @SerialName("message") val message: TelegramMessage? = null,
)

@Serializable
data class TelegramMessage(
    @SerialName("message_id") val messageId: Long,
    @SerialName("from") val from: TelegramUser? = null,
    @SerialName("date") val epochTime: Long,
    @SerialName("chat") val chat: TelegramChat,
    @SerialName("reply_to_message") val replyToTelegramMessage: TelegramMessage? = null,
    @SerialName("text") val text: String? = null,
    @SerialName("location") val location: TelegramLocation? = null,
)

@Serializable
data class TelegramUser(
    @SerialName("id") val id: Long,
    @SerialName("first_name") val firstName: String,
    @SerialName("last_name") val lastName: String? = null,
    @SerialName("username") val username: String? = null
)

@Serializable
data class TelegramChat(
    @SerialName("id") val id: Long,
    @SerialName("type") val type: String,
    @SerialName("title") val title: String? = null,
    @SerialName("username") val username: String? = null,
    @SerialName("first_name") val firstName: String? = null,
    @SerialName("last_name") val lastName: String? = null
)

@Serializable
data class TelegramLocation(
    @SerialName("longitude") val longitude: Double,
    @SerialName("latitude") val latitude: Double
)