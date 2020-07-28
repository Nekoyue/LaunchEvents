package moe.yue.launchlib

import java.time.Instant
import java.time.ZoneId

fun Int.toDate(timeZone: String = "UTC") = Instant.ofEpochSecond(this.toLong()).atZone(ZoneId.of(timeZone)).toString()