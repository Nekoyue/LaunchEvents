package moe.yue.launchlib.telegram.api

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

// ONLY contains basic api types used by the project.
@Serializable
data class Result<T>(
    @SerialName("ok") val ok: Boolean,
    @SerialName("error_code") val errorCode: Int? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("result") val result: T?
)

@Serializable
data class Update(
    @SerialName("update_id") val updateId: Long,
    @SerialName("message") val message: Message? = null,
)

@Serializable
data class Message(
    @SerialName("message_id") val messageId: Long,
    @SerialName("from") val from: User? = null,
    @SerialName("date") val date: Int,
    @SerialName("chat") val chat: Chat,
    @SerialName("reply_to_message") val replyToMessage: Message? = null,
    @SerialName("text") val text: String? = null,
    @SerialName("location") val location: Location? = null,
)

@Serializable
data class User(
    @SerialName("id") val id: Long,
    @SerialName("first_name") val firstName: String,
    @SerialName("last_name") val lastName: String? = null,
    @SerialName("username") val username: String? = null
)

@Serializable
data class Chat(
    @SerialName("id") val id: Long,
    @SerialName("type") val type: String,
    @SerialName("title") val title: String? = null,
    @SerialName("username") val username: String? = null,
    @SerialName("first_name") val firstName: String? = null,
    @SerialName("last_name") val lastName: String? = null
)

@Serializable
data class Location(
    @SerialName("longitude") val longitude: Double,
    @SerialName("latitude") val latitude: Double
)