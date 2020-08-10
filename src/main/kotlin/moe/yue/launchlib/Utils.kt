package moe.yue.launchlib

import java.time.Instant
import java.time.ZoneId
import kotlin.math.roundToInt

val timeUtils = TimeUtils()

class TimeUtils {
    // Convert epoch time to ISO-8601 Time, such as "2020-08-08T15:55:27Z[UTC]"
    fun toTime(epochTime: Long, timeZone: String = "UTC") =
        Instant.ofEpochSecond(epochTime).atZone(ZoneId.of(timeZone)).toString()

    // Convert epoch time to simple ISO-8601 Date, such as "2020-08-08"
    fun toSimpleDate(epochTime: Long, timeZone: String = "UTC") =
        Instant.ofEpochSecond(epochTime).atZone(ZoneId.of(timeZone)).toLocalDate().toString()

    // Convert time to epoch time
    fun toEpochTime(time: String?): Long? = if (time.isNullOrEmpty()) toEpochTime(time) else null
    fun toEpochTime(time: String): Long = Instant.parse(time).epochSecond

    fun minutesToSeconds(minutes: Int): Int = minutes * 60
    fun minutesToSeconds(minutes: Double): Int = (minutes * 60).roundToInt()
    fun hoursToSeconds(hours: Int): Int = hours * 60 * 60
    fun hoursToSeconds(hours: Double): Int = (hours * 60 * 60).roundToInt()
    fun daysToSeconds(days: Int): Int = days * 60 * 60 * 24
    fun daysToSeconds(days: Double): Int = (days * 60 * 60 * 24).roundToInt()

    fun getNow() = Instant.now().epochSecond
}