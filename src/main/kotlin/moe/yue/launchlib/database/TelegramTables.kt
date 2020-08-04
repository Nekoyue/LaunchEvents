package moe.yue.launchlib.database

import moe.yue.launchlib.telegram.api.Message
import moe.yue.launchlib.toDate
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction


object TelegramSentMessages : IntIdTable("sent_messages") {
    val chatId = long("chat_id")
    val messageId = long("message_id")
    val messageDate = varchar("date", 20)
    val text = text("text").nullable()
    val type = varchar("type", 255) // One of the three types: launch, update, listLaunches
    val launchUUID = varchar("launch_library_uuid", 36).nullable() // Null when type is listLaunches
}

data class SentMessages(
    val chatId: Long,
    val messageId: Long,
    val messageDate: String,
    val text: String? = null,
    val type: String,
    val launchUUID: String? = null
)

open class TelegramH2(private val database: Database) {

    fun insertMessage(message: Message, type: String, launchUUID: String?) {
        transaction(database) {
            TelegramSentMessages.insert {
                it[chatId] = message.chat.id
                it[messageId] = message.messageId
                it[messageDate] = message.date.toDate()
                it[text] = message.text
                it[this.type] = type
                it[this.launchUUID] = launchUUID
            }
        }
    }

    fun updateMessage(message: Message, type: String, launchUUID: String?) {
        transaction(database) {
            TelegramSentMessages.update {
                it[chatId] = message.chat.id
                it[messageId] = message.messageId
                it[messageDate] = message.date.toDate()
                it[text] = message.text
                it[this.type] = type
                it[this.launchUUID] = launchUUID
            }
        }
    }

    fun getMessagesByUUID(launchUUID: String): List<SentMessages> {
        val result = mutableListOf<SentMessages>()
        transaction(database) {
            TelegramSentMessages.select { TelegramSentMessages.launchUUID eq launchUUID }.forEach {
                result += SentMessages(
                    chatId = it[TelegramSentMessages.chatId],
                    messageId = it[TelegramSentMessages.messageId],
                    messageDate = it[TelegramSentMessages.messageDate],
                    text = it[TelegramSentMessages.text],
                    type = it[TelegramSentMessages.type],
                    launchUUID = it[TelegramSentMessages.launchUUID]
                )
                it[TelegramSentMessages.chatId]
            }
        }
        return result.sortedByDescending { it.messageDate }
    }

    fun getMessagesByType(type: String): List<SentMessages> {
        val result = mutableListOf<SentMessages>()
        transaction(database) {
            TelegramSentMessages.select { TelegramSentMessages.type eq type }.forEach {
                result += SentMessages(
                    chatId = it[TelegramSentMessages.chatId],
                    messageId = it[TelegramSentMessages.messageId],
                    messageDate = it[TelegramSentMessages.messageDate],
                    text = it[TelegramSentMessages.text],
                    type = it[TelegramSentMessages.type],
                    launchUUID = it[TelegramSentMessages.launchUUID]
                )
            }
        }
        return result.sortedByDescending { it.messageDate }
    }

    fun deleteMessageById(id: Int) {
        transaction(database) {
            TelegramSentMessages.deleteWhere { TelegramSentMessages.id eq id }
        }
    }
}