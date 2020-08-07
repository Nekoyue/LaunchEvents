package moe.yue.launchlib.database

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val h2 = H2()

class H2 {
    private val logger: Logger = LoggerFactory.getLogger("[Database]")

    private val telegramDatabase = Database.connect("jdbc:h2:file:./telegram", driver = "org.h2.Driver")
    private val launchLibDatabase = Database.connect("jdbc:h2:file:./launchlib", driver = "org.h2.Driver")

    init {
        transaction(telegramDatabase) {
            SchemaUtils.createMissingTablesAndColumns(H2Messages)
            H2Messages.selectAll().orderBy(H2Messages.messageEpochTime to SortOrder.ASC)
        }
        transaction(launchLibDatabase) {
            SchemaUtils.createMissingTablesAndColumns(H2Launches)
            H2Launches.selectAll().orderBy(H2Launches.netEpochTime to SortOrder.ASC)
        }
    }

    val telegram = TelegramH2(telegramDatabase)
    val launchLib = LaunchLibH2(launchLibDatabase)
}