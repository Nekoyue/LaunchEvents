package moe.yue.launchlib.telegram

import moe.yue.launchlib.database.H2Launch
import moe.yue.launchlib.launchlib.api.agencyInfo
import moe.yue.launchlib.launchlib.api.flags
import moe.yue.launchlib.telegram.api.botUsername
import moe.yue.launchlib.timeUtils

// Only add if the both strings are not null
infix fun String?.add(string: String?) = if (this.isNullOrEmpty() || string.isNullOrEmpty()) "" else "$this$string"

// Convert MarkdownV2 to HTML, credit https://gist.github.com/jbroadway/2836900
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

fun Boolean?.nullToFalse() = this ?: false

fun H2Launch.text(currentTime: Long = timeUtils.getNow()) = ("" +
        "*${this.name}*" +
        (agencyInfo[this.agencyId]?.let { "\nby ${it.abbrev ?: it.name} ${flags[it.countryCode]}" } ?: "") +
        (this.netEpochTime?.let {
            val countDown = timeUtils.toCountdownTime(it - currentTime)
            val dateTime = timeUtils.toFullTime(it) // UTC date time of a launch
            val status =
                if (this.timeTBD.nullToFalse() || this.dateTBD.nullToFalse() || this.statusDescription == "TBD") "[TBD]"
                else if (this.statusDescription == "Hold") "[Hold]"
                else ""
            "" +
                    "\n\n[[🌐]](https://t.me/$botUsername?start=time$it) " +
                    "$dateTime $status" +
                    "\nT-: $countDown"
        }
            ?: "\nTime TBD") +
        (this.windowEndEpochTime?.let { windowEnd ->
            this.windowStartEpochTime?.let { windowStart ->
                "\nMax hold time: ${timeUtils.toCountdownTime(windowEnd - windowStart)}"
            }
        } ?: "") +
        "\n" +
        (this.padLocationName?.let { "\n[[📍]](https://t.me/$botUsername?start=location${this.padLatitude},${this.padLongitude}) $it\n" }
            ?: "") +
        (this.missionDescription?.let { "\n$it\n" } ?: "") +
        (this.videoUrls?.let { "\nVideo: $it" } ?: "")
        ).toHTML()


fun List<H2Launch>.text(): String {
    var result = "*Listing next launches:*\n\n".toHTML()
    this.forEach {
        result += ("*- ${it.name}*" +
                (agencyInfo[it.agencyId]?.let { info -> "\nby ${info.abbrev ?: info.name} ${flags[info.countryCode]}" }
                    ?: "") +
                it.netEpochTime?.let { netEpochTime ->
                    "\n[[🌐]](https://t.me/$botUsername?start=time$netEpochTime) " +
                            "${timeUtils.toFullTime(netEpochTime)} " +
                            (if (it.timeTBD.nullToFalse() || it.dateTBD.nullToFalse() || it.statusDescription == "TBD") "[TBD]" else "") +
                            (if (it.statusDescription == "Hold") "[Hold]" else "")
                } +
                "\n\n").toHTML()
    }
    return result
}