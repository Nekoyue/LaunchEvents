package moe.yue.launchlib

import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.absoluteValue
import kotlin.math.roundToInt
import kotlin.math.sign

val timeUtils = TimeUtils()

class TimeUtils {
    // Convert epoch time to ISO-8601 UTC Date and Time, such as "2020-08-08 15:55:27"
    fun toTime(epochTime: Long, timeZone: String = "UTC"): String =
        Instant.ofEpochSecond(epochTime).atZone(ZoneId.of(timeZone))
            .run {
                this.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
                    .replaceFirst("T", " ")
                    .replaceFirst("Z[UTC]", "")
            }

    // Convert epoch time to "MMM d, yyyy time", such as "Aug 17, 2020 11:05:30"
    fun toFullTime(epochTime: Long, timeZone: String = "UTC"):String =
        Instant.ofEpochSecond(epochTime).atZone(ZoneId.of(timeZone))
            .run {
                //[0]: dow, [1]: day, [2]: month, [3]: year, [4]: time, [5]: time zone
                // e.g. [0]: Mon, [1]: 17, [2]: Aug, [3]: 2020, [4]: 11:05:30, [5]: GMT
                val components = this.format(DateTimeFormatter.RFC_1123_DATE_TIME)
                    .replaceFirst(",","").split(" ")

                "${components[2]} ${components[1]}, ${components[3]} ${components[4]}"
            }

    // Convert epoch time to simple ISO-8601 UTC Date, such as "2020-08-08"
    fun toSimpleDate(epochTime: Long, timeZone: String = "UTC"): String =
        Instant.ofEpochSecond(epochTime).atZone(ZoneId.of(timeZone)).toLocalDate()
            .run { this.format(DateTimeFormatter.ISO_DATE) }

    // Convert seconds to time period, such as "15:55:27" and "6 days, 5:43:21"
    fun toCountdownTime(seconds: Int): String = toCountdownTime(seconds.toLong())
    fun toCountdownTime(seconds: Long): String {
        var result = ""
        when (val days = seconds / daysToSeconds(1)) {
            0L -> {
            }
            1L -> result += "1 day, "
            else -> result += "${days.absoluteValue} days, "
        }
        result += LocalTime.MIN.plusSeconds(seconds.absoluteValue)
            .run { this.format(DateTimeFormatter.ISO_TIME).removePrefix("0") }
        return "${seconds.sign.toString().dropLast(1)}$result"
    }


    // Convert time to epoch time
    fun toEpochTime(time: String?): Long? = if (time.isNullOrEmpty()) null else toEpochTime(time)
    fun toEpochTime(time: String): Long = Instant.parse(time).epochSecond

    fun minutesToSeconds(minutes: Int): Int = minutes * 60
    fun minutesToSeconds(minutes: Double): Int = (minutes * 60).roundToInt()
    fun hoursToSeconds(hours: Int): Int = hours * 60 * 60
    fun hoursToSeconds(hours: Double): Int = (hours * 60 * 60).roundToInt()
    fun daysToSeconds(days: Int): Int = days * 60 * 60 * 24
    fun daysToSeconds(days: Double): Int = (days * 60 * 60 * 24).roundToInt()

    val now = Instant.now().epochSecond
}