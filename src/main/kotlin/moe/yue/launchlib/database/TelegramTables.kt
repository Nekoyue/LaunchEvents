package moe.yue.launchlib.database

import moe.yue.launchlib.telegram.api.TelegramMessage
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction


object H2Messages : IntIdTable("sent_messages") {
    val chatId = long("chat_id")
    val messageId = long("message_id")
    val messageEpochTime = long("date")
    val text = text("text").nullable()
    val type = varchar("type", 255) // One of three types: launch, update, listLaunches
    val launchUUID = varchar("launch_library_uuid", 36).nullable() // Null when the type is listLaunches
}

// The data class for H2Messages
data class H2Message(
    val index: Int,
    val chatId: Long,
    val messageId: Long,
    val messageEpochTime: Long,
    val text: String? = null,
    val type: String,
    val launchUUID: String? = null
)

open class TelegramH2(private val database: Database) {
    fun insertMessage(telegramMessage: TelegramMessage, type: String, launchUUID: String?) {
        transaction(database) {
            H2Messages.insert {
                it[chatId] = telegramMessage.chat.id
                it[messageId] = telegramMessage.messageId
                it[messageEpochTime] = telegramMessage.epochTime
                it[text] = telegramMessage.text
                it[this.type] = type
                it[this.launchUUID] = launchUUID
            }
        }
    }

    fun updateMessage(telegramMessage: TelegramMessage, type: String, launchUUID: String?) {
        transaction(database) {
            H2Messages.update {
                it[chatId] = telegramMessage.chat.id
                it[messageId] = telegramMessage.messageId
                it[messageEpochTime] = telegramMessage.epochTime
                it[text] = telegramMessage.text
                it[this.type] = type
                it[this.launchUUID] = launchUUID
            }
        }
    }

    private fun ResultRow.toH2Message() = H2Message(
        index = this[H2Messages.id].value,
        chatId = this[H2Messages.chatId],
        messageId = this[H2Messages.messageId],
        messageEpochTime = this[H2Messages.messageEpochTime],
        text = this[H2Messages.text],
        type = this[H2Messages.type],
        launchUUID = this[H2Messages.launchUUID]
    )

    fun getMessages(type: String, launchUUID: String?): List<H2Message> {
        val result = mutableListOf<H2Message>()
        transaction(database) {
            H2Messages.select {
                launchUUID?.let { (H2Messages.type eq type) and (H2Messages.launchUUID eq it) }
                    ?: (H2Messages.type eq type)
            }.forEach {
                result += it.toH2Message()
            }
        }
        return result.sortedByDescending { it.messageEpochTime }
    }

    fun deleteMessage(index: Int) {
        transaction(database) {
            H2Messages.deleteWhere { H2Messages.id eq index }
        }
    }
}