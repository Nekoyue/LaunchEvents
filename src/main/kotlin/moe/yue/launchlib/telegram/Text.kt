package moe.yue.launchlib.telegram

import moe.yue.launchlib.database.H2Launch
import moe.yue.launchlib.launchlib.api.agencyInfo
import moe.yue.launchlib.launchlib.api.flags
import moe.yue.launchlib.telegram.api.botUsername
import moe.yue.launchlib.timeUtils
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.time.ZoneId

// Add two strings if neither is null, otherwise return "" (empty)
infix fun String?.add(string: String?) = if (this.isNullOrEmpty() || string.isNullOrEmpty()) "" else "$this$string"

// https://core.telegram.org/bots/api#html-style
// Convert to safe html, disabled as characters are not rendered correctly
// private val htmlEntities = mapOf("<" to "&lt;", ">" to "&gt;", "&" to "&amp;", "\"" to "&quot;")
// private fun String.toHTML() = htmlEntities.entries.fold(this) { result, (k, v) -> result.replace(k, v) }
private fun String.toHTML() = this

// Formatting
fun String.bold() = "<b>${this.toHTML()}</b>"
fun String.italic() = "<i>${this.toHTML()}</i>"
fun String.underline() = "<u>${this.toHTML()}</u>"
fun String.strike() = "<s>${this.toHTML()}</s>"
fun String.code() = "<code>${this.toHTML()}</code>"
fun String.codeBlock() = "<pre>${this.toHTML()}</pre>"
fun String.hyperlink(url: String) = "<a href='$url'>${this.toHTML()}</a>"

const val noImageAvailable =
    "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/1024px-No_image_available.svg.png"

// Return the first paragraph of a launch's description, ... for the rest.
private fun H2Launch.getShortDescription(): String? = this.missionDescription?.let {
    val shorten = it.substringBefore("\n\n")
    if (shorten.length == it.length)
        it
    else
        "$shorten\n" +
                "[...]".hyperlink("https://t.me/$botUsername?start=details_${this.uuid})")
}

fun H2Launch.detailedText(currentTime: Long = timeUtils.now(), isChannel: Boolean = false) = ("" +
        this.name.bold() +
        (agencyInfo[this.agencyId]?.let { "\n${"by".italic()} ${it.abbrev ?: it.name} ${flags[it.countryCode]}" }
            ?: "") +
        (if (isChannel) "\n${"(Status updated at ${timeUtils.toShortTime(timeUtils.now())})".italic()}" else "") +
        (this.netEpochTime?.let {
            val countDown = timeUtils.toCountdownTime(it - currentTime)
            val dateTime = timeUtils.toFullTime(it)
            val status = this.statusDescription
            (if ((status == "Success" || status == "Fail" || status == "Partial Failure" || status == "In Flight") && isChannel) {
                "\n\n[$status]"
            } else {
                (if (!countDown.startsWith("-")) ("\n\n${"T-:".bold()} $countDown")
                else "\n\n${"T+:".bold()} ${countDown.removePrefix("-")}") + " [$status]"
            }) +
                    "\n${"[🌐Time]".hyperlink("https://t.me/$botUsername?start=time_${this.uuid}")} $dateTime"
        }
            ?: "\nTime TBD") +
        (this.windowEndEpochTime?.let { windowEnd ->
            this.windowStartEpochTime?.let { windowStart ->
                "\nMax holding time: ${timeUtils.toCountdownTime(windowEnd - windowStart)}"
            }
        } ?: "") +
        "\n" +
        (this.padLocationName?.let {
            "\n${"[📍Location]".hyperlink("https://t.me/$botUsername?start=location_${this.uuid}")} ${this.padLocationName}\n"
        }
            ?: "") +
        (this.getShortDescription()?.let { "\n$it\n" } ?: "") +
        (this.videoUrls?.let { "\n${"Live stream:".bold()} $it" } ?: "")
        )


fun List<H2Launch>.listLaunchesText(currentTime: Long = timeUtils.now(), isChannel: Boolean = false): String {
    var result = ("${"Listing next launches:".bold()}\n" +
            (if (isChannel) {
                "(T- based on ${timeUtils.toShortTime(currentTime)} UTC, approx. ${
                    // Hours after the last T- base time
                    timeUtils.toCountdownTime(timeUtils.now() - currentTime)
                        .substringBefore(":").substringAfter(", ").toIntOrNull()
                        ?.let { hours ->
                            if (hours == 0 || hours == 1) "$hours hour"
                            else "$hours hours"
                        } ?: "null hour"
                } ago)\n" +
                        "${"(Status updated at ${timeUtils.toShortTime(timeUtils.now())})".italic()}\n\n"

            } else "\n")
            )
    this.forEach {
        result += ("- ${it.name}".bold() +
                (agencyInfo[it.agencyId]?.let { info -> "\n${"by".italic()} ${info.abbrev ?: info.name} ${flags[info.countryCode]}" }
                    ?: "") +
                it.netEpochTime?.let { netEpochTime ->
                    val countDown = timeUtils.toCountdownTime(netEpochTime - currentTime)
                    val dateTime = timeUtils.toFullTime(netEpochTime) // UTC date time of a launch
                    val status =
                        (if (it.timeTBD == true || it.dateTBD == true || it.statusDescription == "TBD") "[TBD]"
                        else if (it.statusDescription == "Hold") "[Hold]"
                        else "")
                    "\nT-: $countDown $status" +
                            "\n${"[🌐Time]".hyperlink("https://t.me/$botUsername?start=time_${it.uuid}")} $dateTime"
                } +
                "\n\n")
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
                }: ${timeUtils.toShortTime(epochTime, zoneName)} (${
                    // Shorten e.g. "America/Los_Angeles" -> "Los Angeles"
                    zoneName.replace("_", " ").substringAfter("/")
                        .bold()
                })\n"
            }
            return result
        }
        ("${"Time Zone Converter".bold()} for mission\n" +
                "${name.bold()}\n\n" +
                (if (timeTBD == true || dateTBD == true || statusDescription == "TBD") "Exact time to be decided\n"
                else "") +
                listOf(
                    "America/Los_Angeles",
                    "America/New_York",
                    "America/Sao_Paulo"
                ).convert() +
                "UTC+0: ${
                    timeUtils.toFullTime(epochTime).split(" ").run { "${this[0]} ${this[1]} ${this[3]}" }
                } (Greenwich)".bold() + "\n" +
                listOf(
                    "Europe/London",
                    "Europe/Paris",
                    "Europe/Moscow",
                    "Asia/Singapore",
                    "Asia/Tokyo",
                    "Australia/Sydney",
                    "Pacific/Auckland"
                ).convert() +
                "\n" + "[Other Time Zones]".hyperlink(
            "https://www.thetimezoneconverter.com/?t=${
                timeUtils.toTime(epochTime).substringAfter(" ")
                    .substringBeforeLast(":") // convert to e.g. 12:34
            }&tz=UTC"
        ))
    }
}

fun H2Launch.locationText(): String =
    ("${this.name.bold()}'s launch site is located at\n" +
            this.padLocationName
            + (this.padWikiUrl?.let { "\n${URLDecoder.decode(it, StandardCharsets.UTF_8.name())}" } ?: ""))
