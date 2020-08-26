package moe.yue.launchlib.telegram

import moe.yue.launchlib.database.H2Launch
import moe.yue.launchlib.launchlib.api.agencyInfo
import moe.yue.launchlib.launchlib.api.flags
import moe.yue.launchlib.telegram.api.botUsername
import moe.yue.launchlib.timeUtils
import java.time.Instant
import java.time.ZoneId

// Add two strings if neither is null, otherwise return "" (empty)
infix fun String?.add(string: String?) = if (this.isNullOrEmpty() || string.isNullOrEmpty()) "" else "$this$string"

// Convert MarkdownV2 to HTML, inspired by https://gist.github.com/jbroadway/2836900
fun String.toHTML() = this
    .replace(Regex("\\[(.*)\\]\\(([^\\)]+)\\)")) { "<a href='${it.groupValues[2]}'>${it.groupValues[1]}</a>" } // links
    .replace(Regex("(__)(.*?[^\\\\\\n])\\1")) { "<u>${it.groupValues[2].replace("\\_", "_")}</u>" } // underline
    .replace(Regex("(\\*)(.*?[^\\\\\\n])\\1")) { "<b>${it.groupValues[2].replace("\\*", "*")}</b>" } // bold
    .replace(Regex("(_)(.*?[^\\\\\\n])\\1")) { "<i>${it.groupValues[2].replace("\\_", "_")}</i>" } // italic
    .replace(Regex("\\~(.*?[^\\\\])\\~")) { "<s>${it.groupValues[1].replace("\\~", "~")}</s>" } // strike
    .replace(Regex("\\`\\`\\`(.*\\n[\\s\\S]*?\\n)\\`\\`\\`")) { "<pre>${it.groupValues[1]}</pre>" } // code block
    .replace(Regex("(\\`)(.*?[^\\\\\\n])\\1")) {
        "<code>${it.groupValues[2].replace("\\`", "`")}</code>"
    } // inline code

// Return the first paragraph of a launch's description, ... for the rest.
private fun H2Launch.getShortDescription(): String? = this.missionDescription?.let {
    val shorten = it.substringBefore("\n\n")
    if (shorten.length == it.length)
        it
    else
        "$shorten\n" +
                "[[...]](https://t.me/$botUsername?start=details_${this.uuid})"
}

fun H2Launch.detailedText(currentTime: Long = timeUtils.now(), updatable: Boolean = false) = ("" +
        "*${this.name}*" +
        (if (updatable) "_(Last updated at ${timeUtils.toShortTime(timeUtils.now())})_\n\n" else "") +
        (agencyInfo[this.agencyId]?.let { "\n_by_ ${it.abbrev ?: it.name} ${flags[it.countryCode]}" } ?: "") +
        (this.netEpochTime?.let {
            val countDown = timeUtils.toCountdownTime(it - currentTime)
            val dateTime = timeUtils.toFullTime(it)
            val status = this.statusDescription
            (if (!countDown.startsWith("-")) ("\n\n*T-:* $countDown")
            else "\n\n*T+:* ${countDown.removePrefix("-")}") +
                    " [$status]\n[[🌐]](https://t.me/$botUsername?start=time_${this.uuid}) $dateTime"
        }
            ?: "\nTime TBD") +
        (this.windowEndEpochTime?.let { windowEnd ->
            this.windowStartEpochTime?.let { windowStart ->
                "\nMax holding time: ${timeUtils.toCountdownTime(windowEnd - windowStart)}"
            }
        } ?: "") +
        "\n" +
        (this.padLocationName?.let {
            "\n[[📍]](https://t.me/$botUsername?start=location_${this.uuid}) ${
                this.padWikiUrl
                    ?.substringAfter("/wiki/", "")?.run { if (this.isEmpty()) null else this }
                    ?.replace("_", " ")
                    ?.let { padName -> "$padName \n_in_ " }
            }$it\n"
        }
            ?: "") +
        (this.getShortDescription()?.let { "\n$it\n" } ?: "") +
        (this.videoUrls?.let { "\n*Video:* $it" } ?: "")
        ).toHTML()


fun List<H2Launch>.listLaunchesText(currentTime: Long = timeUtils.now(), updatable: Boolean = false): String {
    var result = ("*Listing next launches:*\n" +
            (if (updatable) "_(Last updated at ${timeUtils.toShortTime(timeUtils.now())})_\n\n" else "")
            ).toHTML()
    this.forEach {
        result += ("*- ${it.name}*" +
                (agencyInfo[it.agencyId]?.let { info -> "\nby ${info.abbrev ?: info.name} ${flags[info.countryCode]}" }
                    ?: "") +
                it.netEpochTime?.let { netEpochTime ->
                    val countDown = timeUtils.toCountdownTime(netEpochTime - currentTime)
                    val dateTime = timeUtils.toFullTime(netEpochTime) // UTC date time of a launch
                    val status =
                        (if (it.timeTBD == true || it.dateTBD == true || it.statusDescription == "TBD") "[TBD]"
                        else if (it.statusDescription == "Hold") "[Hold]"
                        else "")
                    "\nT-: $countDown $status" +
                            "\n[[🌐]](https://t.me/$botUsername?start=time_${it.uuid}) $dateTime"
                } +
                "\n\n").toHTML()
    }
    return result
}

fun H2Launch.timeZoneConverterText(): String? {
    val epochTime = this.netEpochTime
    return epochTime?.let {
        // Convert to local time given a list of time zones and the epochTime
        // e.g. "America/Los_Angeles" -> "Los Angeles (UTC-8): Aug 17, 03:05:30"
        fun List<String>.convert(): String {
            var result = ""
            forEach { zoneName ->
                result += "UTC${
                    ZoneId.of(zoneName).rules.getOffset(Instant.ofEpochSecond(epochTime)).toString()
                        // Shorten e.g. "+08:00" -> "+8"
                        .removeSuffix(":00").replace("-0", "-").replace("+0", "+")
                }: ${timeUtils.toShortTime(epochTime, zoneName)} (*${
                    // Shorten e.g. "America/Los_Angeles" -> "Los Angeles"
                    zoneName.replace("_", " ").substringAfter("/")
                }*)\n"
            }
            return result
        }
        ("*Time Zone Converter* for mission\n" +
                "*$name*\n\n" +
                (if (timeTBD == true || dateTBD == true || statusDescription == "TBD") "Exact time to be decided\n"
                else "") +
                listOf(
                    "America/Los_Angeles",
                    "America/New_York",
                    "America/Sao_Paulo"
                ).convert() +
                "*UTC+0: ${
                    timeUtils.toFullTime(epochTime).split(" ").run { "${this[0]} ${this[1]} ${this[3]}" }
                } (Greenwich)*\n" +
                listOf(
                    "Europe/London",
                    "Europe/Paris",
                    "Europe/Moscow",
                    "Asia/Singapore",
                    "Asia/Tokyo",
                    "Australia/Sydney",
                    "Pacific/Auckland"
                ).convert() +
                "\n[Other Time Zones](https://www.thetimezoneconverter.com/?t=${
                    timeUtils.toTime(epochTime).substringAfter(" ")
                        .substringBeforeLast(":") // convert to e.g. 12:34
                }&tz=UTC)")
            .toHTML()
    }
}

fun H2Launch.locationText(): String =
    ("*${this.name}*'s launch site is located\n" +
            this.padWikiUrl
                ?.substringAfter("/wiki/", "")?.run { if (this.isEmpty()) null else this }
                ?.replace("_", " ")
                ?.let { "_at_ $it\n" } +
            this.padLocationName.let { "_in_ $it" }).toHTML()
