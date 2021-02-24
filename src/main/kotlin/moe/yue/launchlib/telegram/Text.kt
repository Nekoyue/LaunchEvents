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

// Replace symbols with safe html entities. Currently disabled.
// https://core.telegram.org/bots/api#html-style
// private val htmlEntities = mapOf("<" to "&lt;", ">" to "&gt;", "&" to "&amp;", "\"" to "&quot;")
// private fun String.toHTML() = htmlEntities.entries.fold(this) { result, (k, v) -> result.replace(k, v) }
private fun String.toHTML() = this

// Formatting codes
fun String.bold() = "<b>${this.toHTML()}</b>"
fun String.italic() = "<i>${this.toHTML()}</i>"
fun String.underline() = "<u>${this.toHTML()}</u>"
fun String.strike() = "<s>${this.toHTML()}</s>"
fun String.code() = "<code>${this.toHTML()}</code>"
fun String.codeBlock() = "<pre>${this.toHTML()}</pre>"
fun String.hyperlink(url: String) = "<a href='$url'>${this.toHTML()}</a>"

const val placeholderLineTBD = "[Placeholder line to be deleted]"

fun String.removePlaceholderLines() = this.lines()
    .filterNot { it.contains(placeholderLineTBD) }
    .joinTo(StringBuffer(""), "\n").toString()

// Combine two strings if neither is null, return "" (empty) otherwise.
infix fun String?.add(string: String?) = if (this.isNullOrEmpty() || string.isNullOrEmpty()) "" else "$this$string"

const val noImageAvailableUrl =
    "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/1024px-No_image_available.svg.png"

// Return the first paragraph (or the leading 600 characters if too long) of a launch's description.
// The rest is displayed as ... (ellipsis) linked to bot's /start command.
private fun H2Launch.getShortDescription(): String? = this.missionDescription?.let {
    val shorten = it.substringBefore("\r\n\r\n").take(600)
    if (shorten.length == it.length)
        it
    else
        "$shorten\n" +
                "[...]".hyperlink("https://t.me/$botUsername?start=details_${this.uuid}")
}

// Return name and flag of an agency
private fun H2Launch.getAgencyInfoText() = agencyInfo[this.agencyId]
    ?.let { "${it.abbrev ?: it.name} ${flags[it.countryCode]}" }

// Return the time of launch relative to currentTime
private fun H2Launch.getTMinusText(currentTime: Long) = this.netEpochTime
    ?.let {
        val countDown = timeUtils.toCountdownTime(it - currentTime)
        if (countDown.startsWith("-"))
            "${"T+:".bold()} ${countDown.removePrefix("-")}"
        else
            ("${"T-:".bold()} $countDown")
    }

private fun H2Launch.getLaunchTimeText() = this.netEpochTime
    ?.let { timeUtils.toFullTime(it) }


// T-Minus is hidden with following status.
private val hideTMinusConditions = listOf("Success", "Failure", "Partial Failure", "In Flight")

fun H2Launch.detailText(currentTime: Long = timeUtils.now(), isChannel: Boolean = false) = """
    |${this.name.bold()}
    |${"by".italic()} ${this.getAgencyInfoText() ?: placeholderLineTBD}
    |
    |${if (this.netEpochTime == null) "[Time TBD]" else placeholderLineTBD}
    |${
    if (!hideTMinusConditions.contains(this.statusDescription))
        (this.getTMinusText(currentTime) ?: placeholderLineTBD)
    else ""
} [${this.statusDescription}]
    |${"[🌐]".hyperlink("https://t.me/$botUsername?start=time_${this.uuid}")} ${this.getLaunchTimeText() ?: placeholderLineTBD}
    |${
    if (this.windowEndEpochTime != null && this.windowStartEpochTime != null)
        "Max holding time: ${timeUtils.toCountdownTime(this.windowEndEpochTime - this.windowStartEpochTime)}"
    else placeholderLineTBD
} 
    |
    |${"[📍]".hyperlink("https://t.me/$botUsername?start=location_${this.uuid}")} ${this.padLocationName ?: placeholderLineTBD}
    |
    |${this.missionType.let { if (it.isNullOrBlank()) "" else "[$it] ".bold() }}${this.getShortDescription() ?: placeholderLineTBD}
    |
    |${if (isChannel) "(Status updated at ${timeUtils.toShortTime(timeUtils.now())} UTC)".italic() else ""}
""".trimMargin().removePlaceholderLines()


fun List<H2Launch>.listLaunchesText(currentTime: Long = timeUtils.now(), isChannel: Boolean = false) = """
        |${"Listing next launches:".bold()}
        |${
    if (isChannel)
        "(T- based on ${timeUtils.toShortTime(currentTime)} UTC, approx. ${
            // Hours since the beginning of current UTC day.
            timeUtils.secondsToHours(timeUtils.now() - currentTime)
                .let { if (it == 1) "$it hour" else "$it hours" }
        } ago)"
    else placeholderLineTBD
}
    |
    |${
    this.fold("") { result, it ->
        (result + """
            |
            |${"- ${it.name}".bold()}
            |${"by".italic()} ${it.getAgencyInfoText() ?: placeholderLineTBD}
            |${it.getTMinusText(currentTime) ?: placeholderLineTBD} ${
            if (it.statusDescription == "TBD" || it.statusDescription == "TBC")
                "[${it.statusDescription}]" else ""
        }
            |${"[🌐]".hyperlink("https://t.me/$botUsername?start=time_${it.uuid}")} ${it.getLaunchTimeText() ?: placeholderLineTBD}
        """).trimMargin()
    }
}
    |
    |${if (isChannel) "(Status updated at ${timeUtils.toShortTime(timeUtils.now())} UTC)".italic() else ""}
    """.trimMargin().removePlaceholderLines()

fun H2Launch.timeZoneConverterText(): String? {
    val epochTime = this.netEpochTime
    return epochTime?.let {
        // Convert to local time given a list of time zones and the epochTime
        // e.g. "America/Los_Angeles" -> "Los Angeles (UTC-8): Aug 17, 03:05:30"
        fun List<String>.convert(): String {
            var result = ""
            forEach { zoneName ->
                result += "|UTC${
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
        """
            |${"Time Zone Converter".bold()} for mission
            |${name.bold()}
            |${if (statusDescription == "TBD" || statusDescription == "TBC") "Exact time to be decided" else placeholderLineTBD}
            |
            ${
            listOf(
                "America/Los_Angeles",
                "America/New_York",
                "America/Sao_Paulo"
            ).convert()
        }|${
            "UTC+0: ${
                timeUtils.toFullTime(epochTime).split(" ").run { "${this[0]} ${this[1]} ${this[3]}" }
            } (Greenwich)".bold()
        }
            ${
            listOf(
                "Europe/London",
                "Europe/Paris",
                "Europe/Moscow",
                "Asia/Singapore",
                "Asia/Tokyo",
                "Australia/Sydney",
                "Pacific/Auckland"
            ).convert()
        }
            |${
            "[Other Time Zones]".hyperlink(
                "https://www.thetimezoneconverter.com/?t=${
                    timeUtils.toTime(epochTime).substringAfter(" ")
                        .substringBeforeLast(":") // convert to e.g. 12:34
                }&tz=UTC"
            )
        }
        """.trimMargin().removePlaceholderLines()
    }
}

fun H2Launch.locationText() =
    """
        |${this.name.bold()}'s launch site is located at
        |${this.padLocationName}
        |${this.padWikiUrl?.let { URLDecoder.decode(it, StandardCharsets.UTF_8.name()) } ?: placeholderLineTBD}
        """.trimMargin().removePlaceholderLines()