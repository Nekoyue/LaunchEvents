package moe.yue.launchlib.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

val h2 = H2()

class H2 {
    private val telegramDatabase = Database.connect("jdbc:h2:file:./telegram", driver = "org.h2.Driver")
    private val launchLibDatabase = Database.connect("jdbc:h2:file:./launchlib", driver = "org.h2.Driver")

    init {
        transaction(telegramDatabase) {
            SchemaUtils.createMissingTablesAndColumns(H2MessagesTable)
            H2MessagesTable.selectAll().orderBy(H2MessagesTable.messageEpochTime to SortOrder.ASC)
        }
        transaction(launchLibDatabase) {
            SchemaUtils.createMissingTablesAndColumns(H2LaunchesTable)
            H2LaunchesTable.selectAll().orderBy(H2LaunchesTable.netEpochTime to SortOrder.ASC)
        }
    }

    val telegram = TelegramH2(telegramDatabase)
    val launchLib = LaunchLibH2(launchLibDatabase)
}