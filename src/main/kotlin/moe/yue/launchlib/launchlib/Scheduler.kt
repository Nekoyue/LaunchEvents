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

// The epoch time of the last Launch Library update
var lastUpdate = 0L

// Regular Launch LaunchLibrary updates interval
val regularUpdateInterval = timeUtils.minutesToSeconds(30)

// Update interval before launches, MutableMap<T-minus, Update interval>
val tMinusUpdateIntervals = mutableMapOf(
    timeUtils.minutesToSeconds(120) to timeUtils.minutesToSeconds(10),
    timeUtils.minutesToSeconds(60) to timeUtils.minutesToSeconds(5),
    timeUtils.minutesToSeconds(20) to timeUtils.minutesToSeconds(2),
    timeUtils.minutesToSeconds(5) to timeUtils.minutesToSeconds(0.5)
).toSortedMap()

// Update interval after launches, Map<T-plus, Update interval>
val tPlusUpdateIntervals = mutableMapOf(
    timeUtils.minutesToSeconds(5) to timeUtils.minutesToSeconds(0.5),
    timeUtils.minutesToSeconds(20) to timeUtils.minutesToSeconds(2),
    timeUtils.minutesToSeconds(60) to timeUtils.minutesToSeconds(5),
    timeUtils.minutesToSeconds(120) to timeUtils.minutesToSeconds(10)
).toSortedMap()

// Get the epoch time for the next update
fun nextUpdateTime(): Long {
    val checkRange = timeUtils.daysToSeconds(1)

    val nextLaunchEpochTime =
        h2.launchLib.getRecentLaunches(0, checkRange).firstOrNull()?.netEpochTime
    // Seconds util the upcoming launch
    val tMinus = nextLaunchEpochTime?.minus(timeUtils.getNow())?.absoluteValue

    val lastLaunchEpochTime =
        h2.launchLib.getRecentLaunches(checkRange, 0).lastOrNull()?.netEpochTime
    // Seconds after last launch
    val tPlus = lastLaunchEpochTime?.minus(timeUtils.getNow())?.absoluteValue

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

// A new list of launches will be sent after 2 days
val listLaunchesMaxInterval = timeUtils.daysToSeconds(2)

// Number of launches per listLaunches message
const val listLaunchesLimit = 5

suspend fun scheduler() {
    while (true) {
        // Update database from Launch Library
        val nextUpdateTime = nextUpdateTime()
        if (nextUpdateTime < timeUtils.getNow()) {
            logger.info {
                "Updating launch library with ${timeUtils.toCountdownTime(timeUtils.getNow() - nextUpdateTime)} delay"
            }
            launchLib.get().forEach {
                val oldData = h2.launchLib.getLaunch(it.uuid)
                val newData = h2.launchLib.addLaunch(it)
                oldData?.let { updateLaunch(oldData, newData) }
            }
            lastUpdate = timeUtils.getNow()
            logger.info { "Next update within ${timeUtils.toCountdownTime(nextUpdateTime() - timeUtils.getNow())}" }
        }

        // Send new launches within following 2 hours
        h2.launchLib.getRecentLaunches(0, timeUtils.hoursToSeconds(2)).forEach {
            if (h2.telegram.getMessages("launch", it.uuid).isEmpty()) telegramChannel.newLaunch(it)
        }

        // Send a list of upcoming launches
        if (
        // either one hours after a launch
            (h2.launchLib.getRecentLaunches(0, timeUtils.hoursToSeconds(2)).size -
                    h2.launchLib.getRecentLaunches(0, timeUtils.hoursToSeconds(1)).size >= 1 // There is a launch between 1 and 2 hours
                    && h2.telegram.getMessages("listLaunches").lastOrNull()?.messageEpochTime ?: 0
                    <= timeUtils.getNow() - timeUtils.hoursToSeconds(2) // No previous messages was sent
                    )
                .also { if (it) logger.info { "Preparing to list launches: one hour after previous launch" } }
            // or after a period of listLaunchesMaxInterval
            || (h2.telegram.getMessages("listLaunches").lastOrNull()?.messageEpochTime ?: 0
                    <= timeUtils.getNow() - listLaunchesMaxInterval)
                .also { if (it) logger.info { "Preparing to list launches: listLaunchesMaxInterval expired" } }
        ) {
            h2.launchLib.getRecentLaunches(0, timeUtils.daysToSeconds(60)).run {
                telegramChannel.listLaunches(if (this.size <= 5) this else this.take(listLaunchesLimit))
            }
        }
        delay(20000)
    }
}

fun updateLaunch(old: H2Launch, new: H2Launch) {
    val differences = h2.launchLib.findDifferences(old, new)
        // These keys are unnecessary as the values always increase after any launches
        .filter { it.key != "padLocationTotalLaunchCount" && it.key != "padTotalLaunchCount" }
        .toMutableMap()
    if (differences.isNotEmpty())
        telegramChannel.updateLaunch(new.uuid, differences)
}

private val logger = KotlinLogging.logger("[${timeUtils.toTime(timeUtils.getNow())}] Launch Library Scheduler")