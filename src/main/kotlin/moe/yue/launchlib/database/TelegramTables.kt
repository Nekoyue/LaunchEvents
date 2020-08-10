package moe.yue.launchlib.database

import moe.yue.launchlib.telegram.api.TelegramMessage
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction


object H2MessagesTable : Table("sent_messages") {
    val index = integer("index").uniqueIndex().autoIncrement()
    val chatId = long("chat_id")
    val messageId = long("message_id")
    val messageEpochTime = long("date")
    val text = text("text").nullable()
    val type = varchar("type", 255) // Available types: launch, update, listLaunches
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
    // Update the message if existed, otherwise insert a new message
    fun addMessage(telegramMessage: TelegramMessage, type: String, launchUUID: String?): H2Message {
        val query = getMessageById(telegramMessage.chat.id, telegramMessage.messageId)
        return if (query == null)
            insertMessage(telegramMessage, type, launchUUID)
        else
            updateMessage(query.index, telegramMessage, type, launchUUID)!!
    }

    private fun insertMessage(telegramMessage: TelegramMessage, type: String, launchUUID: String?): H2Message {
        var index = -1
        transaction(database) {
            index = H2MessagesTable.insert {
                it[chatId] = telegramMessage.chat.id
                it[messageId] = telegramMessage.messageId
                it[messageEpochTime] = telegramMessage.epochTime
                it[text] = telegramMessage.text
                it[this.type] = type
                it[this.launchUUID] = launchUUID
            } get H2MessagesTable.index
        }
        return getMessageByIndex(index)!!
    }

    private fun updateMessage(
        index: Int,
        telegramMessage: TelegramMessage, type: String, launchUUID: String?
    ): H2Message? {
        transaction(database) {
            H2MessagesTable.update({ H2MessagesTable.index eq index }) {
                it[chatId] = telegramMessage.chat.id
                it[messageId] = telegramMessage.messageId
                it[messageEpochTime] = telegramMessage.epochTime
                it[text] = telegramMessage.text
                it[this.type] = type
                it[this.launchUUID] = launchUUID
            }
        }
        return getMessageByIndex(index)
    }

    // Convert H2MessagesTable.select{...}.[n] to H2Message
    private fun ResultRow.toH2Message() = H2Message(
        index = this[H2MessagesTable.index],
        chatId = this[H2MessagesTable.chatId],
        messageId = this[H2MessagesTable.messageId],
        messageEpochTime = this[H2MessagesTable.messageEpochTime],
        text = this[H2MessagesTable.text],
        type = this[H2MessagesTable.type],
        launchUUID = this[H2MessagesTable.launchUUID]
    )

    private fun getMessageByIndex(index: Int): H2Message? {
        var result: H2Message? = null
        transaction(database) {
            result =
                H2MessagesTable.select { H2MessagesTable.index eq index }.withDistinct().firstOrNull()?.toH2Message()
        }
        return result
    }

    private fun getMessageById(chatId: Long, messageId: Long): H2Message? {
        var result: H2Message? = null
        transaction(database) {
            result =
                H2MessagesTable.select { (H2MessagesTable.chatId eq chatId) and (H2MessagesTable.messageId eq messageId) }
                    .withDistinct().firstOrNull()?.toH2Message()
        }
        return result
    }

    // Available types: launch, update, listLaunches
    fun getMessages(type: String, launchUUID: String? = null): List<H2Message> {
        val result = mutableListOf<H2Message>()
        transaction(database) {
            H2MessagesTable.select {
                // Only search for type if launchUUID is null
                launchUUID?.let { (H2MessagesTable.type eq type) and (H2MessagesTable.launchUUID eq it) }
                    ?: (H2MessagesTable.type eq type)
            }.forEach {
                result += it.toH2Message()
            }
        }
        return result.sortedBy { it.messageEpochTime }
    }

    fun deleteMessage(index: Int) {
        transaction(database) {
            H2MessagesTable.deleteWhere { H2MessagesTable.index eq index }
        }
    }
}