package moe.yue.launchlib.launchlib

import kotlinx.coroutines.delay
import moe.yue.launchlib.database.H2Launch
import moe.yue.launchlib.database.h2
import moe.yue.launchlib.launchlib.api.launchLib
import moe.yue.launchlib.telegram.telegramChannel
import moe.yue.launchlib.timeUtils
import mu.KotlinLogging
import kotlin.math.absoluteValue
import kotlin.math.min

// Regular Launch LaunchLibrary updates interval
val regularUpdateInterval = timeUtils.minutesToSeconds(30)

// Update interval before launches, MutableMap<T-minus, Update interval>
val tMinusUpdateIntervals = mutableMapOf(
    timeUtils.minutesToSeconds(120) to timeUtils.minutesToSeconds(10),
    timeUtils.minutesToSeconds(60) to timeUtils.minutesToSeconds(6),
    timeUtils.minutesToSeconds(20) to timeUtils.minutesToSeconds(3),
    timeUtils.minutesToSeconds(5) to timeUtils.minutesToSeconds(1)
).toSortedMap()

// Update interval after launches, Map<T-plus, Update interval>
val tPlusUpdateIntervals = mutableMapOf(
    timeUtils.minutesToSeconds(5) to timeUtils.minutesToSeconds(1),
    timeUtils.minutesToSeconds(20) to timeUtils.minutesToSeconds(3),
    timeUtils.minutesToSeconds(60) to timeUtils.minutesToSeconds(6),
    timeUtils.minutesToSeconds(120) to timeUtils.minutesToSeconds(10)
).toSortedMap()

// The epoch time of the last Launch Library update
var lastUpdate = 0L

// Get the epoch time for the next update for launch
fun nextUpdateTime(): Long {
    val checkRange = timeUtils.daysToSeconds(1)

    val nextLaunchEpochTime =
        h2.launchLib.getRecentLaunches(0, checkRange).firstOrNull()?.netEpochTime
    val tMinus = nextLaunchEpochTime?.minus(timeUtils.now())?.absoluteValue // Seconds util the upcoming launch

    val lastLaunchEpochTime =
        h2.launchLib.getRecentLaunches(checkRange, 0).lastOrNull()?.netEpochTime
    val tPlus = lastLaunchEpochTime?.minus(timeUtils.now())?.absoluteValue // Seconds after the last launch

    // Seconds until next update
    val tMinusUpdateInterval = tMinus?.let {
        tMinusUpdateIntervals.filter { it.key >= tMinus }.values.firstOrNull()
    } ?: regularUpdateInterval
    val tPlusUpdateInterval = tPlus?.let {
        tPlusUpdateIntervals.filter { it.key >= tPlus }.values.firstOrNull()
    } ?: regularUpdateInterval
    val updateInterval = min(tMinusUpdateInterval, tPlusUpdateInterval).toLong()

    return lastUpdate + updateInterval
}

//// A listLaunches message will be sent every 1.8 days
//val listLaunchesMaxInterval = timeUtils.daysToSeconds(1.8)

// The listLaunches message will be updated every 15 seconds
val listLaunchesUpdateInterval = timeUtils.minutesToSeconds(15)
var lastListLaunchesUpdate = 0L

// Number of launches per listLaunches message
const val listLaunchesLimit = 5

fun nextListLaunchesUpdateTime(): Long = lastListLaunchesUpdate + listLaunchesUpdateInterval

suspend fun scheduler() {
    while (true) {
        // Update database from Launch Library
        val nextUpdateTime = nextUpdateTime()
        if (nextUpdateTime < timeUtils.now()) {
            logger().info {
                "Updating launch library with ${timeUtils.toCountdownTime(timeUtils.now() - nextUpdateTime)} delay"
            }
            launchLib.get().forEach {
                val oldData = h2.launchLib.getLaunch(it.uuid)
                val newData = h2.launchLib.addLaunch(it)
                oldData?.let { updateLaunch(oldData, newData) }
            }
            lastUpdate = timeUtils.now()
            logger().info { "Next update within ${timeUtils.toCountdownTime(nextUpdateTime() - timeUtils.now())}" }
        }

        // Send upcoming launches in the following 2 hours
        h2.launchLib.getRecentLaunches(0, timeUtils.hoursToSeconds(2)).forEach {
            val launchMessages = h2.telegram.getMessages("launch", it.uuid)
            if (launchMessages.isEmpty()) // either if it had never been sent
                telegramChannel.newLaunch(it)
            else if (launchMessages.lastOrNull()?.messageEpochTime ?: Long.MAX_VALUE
                < timeUtils.now() - timeUtils.daysToSeconds(1)
            )   // or if the launch was sent one day before
            {
                telegramChannel.newLaunch(it, launchMessages)
                // Change type from launch to launchRedirected
                launchMessages.forEach { launch ->
                    h2.telegram.addMessage(launch, "launchRedirected")
                }
            }
        }

        // Update the listLaunches message
        val nextListLaunchesUpdateTime = nextListLaunchesUpdateTime()
        if (nextListLaunchesUpdateTime < timeUtils.now()) {
            logger().info { "List launches message updated" }
            telegramChannel.updateListLaunches()
            lastListLaunchesUpdate = timeUtils.now()
        }

//        // Send a listLaunches message
//        if (
////        // (Commented as not working properly) either three hours after a launch
////            (h2.telegram.getMessages("launch").lastOrNull()?.messageEpochTime ?: 0
////                    >= timeUtils.now() - timeUtils.hoursToSeconds(5)
////                    && h2.telegram.getMessages("listLaunches").lastOrNull()?.messageEpochTime ?: 0
////                    <= timeUtils.now() - timeUtils.hoursToSeconds(12) // No other listLaunches messages were sent
////                    )
////                .also { if (it) logger().info { "Preparing to list launches: one hour after the previous launch" } } ||
//        // or after period with listLaunchesMaxInterval seconds
//            (h2.telegram.getMessages("listLaunches").lastOrNull()?.messageEpochTime ?: 0
//                    <= timeUtils.now() - listLaunchesMaxInterval)
//                .also { if (it) logger().info { "Preparing to list launches: maximum interval expired" } }
//        ) {
//            newListLaunches()
//        }

        delay(20000)
    }
}

fun updateLaunch(old: H2Launch, new: H2Launch) {
    val differences = h2.launchLib.findDifferences(old, new)
        // Remove unnecessary keys as the values always increase after certain launches
        .filter { it.key != "padLocationTotalLaunchCount" && it.key != "padTotalLaunchCount" }
        .toMutableMap()
    if (differences.isNotEmpty())
        telegramChannel.updateLaunch(new.uuid, differences)
}

fun newListLaunches() {
    h2.launchLib.getRecentLaunches(0, timeUtils.daysToSeconds(60)).run {
        telegramChannel.listLaunches(if (this.size <= listLaunchesLimit) this else this.take(listLaunchesLimit))
    }
}

private fun logger() = KotlinLogging.logger("[${timeUtils.toTime(timeUtils.now())}] Launch Library Scheduler")