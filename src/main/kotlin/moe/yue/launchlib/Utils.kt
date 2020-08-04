package moe.yue.launchlib

import java.time.Instant
import java.time.ZoneId

val timeUtils = TimeUtils()

class TimeUtils {
    // Convert epoch time to Time
    fun toTime(epochTime: Long, timeZone: String = "UTC") =
        Instant.ofEpochSecond(epochTime).atZone(ZoneId.of(timeZone)).toString()

    // Convert time to epoch time
    fun toEpochTime(time: String?): Long? = if(time.isNullOrEmpty()) toEpochTime(time) else null
    fun toEpochTime(time: String): Long = Instant.parse(time).epochSecond

}