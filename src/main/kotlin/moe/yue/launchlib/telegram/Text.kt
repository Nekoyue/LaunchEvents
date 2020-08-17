package moe.yue.launchlib.telegram

import moe.yue.launchlib.database.H2Launch
import moe.yue.launchlib.telegram.api.botUsername
import moe.yue.launchlib.timeUtils

// Only add if the both strings are not null
infix fun String?.add(string: String?) = if (this.isNullOrEmpty() || string.isNullOrEmpty()) "" else "$this$string"

// Convert MarkdownV2 to HTML, credit https://gist.github.com/jbroadway/2836900
fun String.toHTML() = this
    .replace(Regex("\\[([^\\[]+)]\\(([^\\)]+)\\)")) { "<a href='${it.groupValues[2]}'>${it.groupValues[1]}</a>" } // links
    .replace(Regex("(__)(.*?[^\\\\\\n])\\1")) { "<u>${it.groupValues[2].replace("\\_", "_")}</u>" } // underline
    .replace(Regex("(\\*)(.*?[^\\\\\\n])\\1")) { "<b>${it.groupValues[2].replace("\\*", "*")}</b>" } // bold
    .replace(Regex("(_)(.*?[^\\\\\\n])\\1")) { "<i>${it.groupValues[2].replace("\\_", "_")}</i>" } // italic
    .replace(Regex("\\~(.*?[^\\\\])\\~")) { "<s>${it.groupValues[1].replace("\\~", "~")}</s>" } // strike
    .replace(Regex("\\`\\`\\`(.*\\n[\\s\\S]*?\\n)\\`\\`\\`")) { "<pre>${it.groupValues[1]}</pre>" } // code block
    .replace(Regex("(\\`)(.*?[^\\\\\\n])\\1")) {
        "<code>${it.groupValues[2].replace("\\`", "`")}</code>"
    } // inline code

fun Boolean?.nullToFalse() = this ?: false

fun H2Launch.text(currentTime: Long = timeUtils.getNow()) = """
    *${this.name}*
    ${
    this.netEpochTime?.let {
        val countDown = timeUtils.toCountdownTime(it - currentTime)
        val dateTime = timeUtils.toTime(it) // UTC date time of a launch
        val tbd = if (this.timeTBD.nullToFalse() || this.dateTBD.nullToFalse()) "[TBD]" else ""
        "T-: $countDown ([_${dateTime}_](https://t.me/$botUsername?start=time:$dateTime)) $tbd"
    }
        ?: "Time TBD"
}
    test
""".trimIndent().toHTML()


fun List<H2Launch>.text(): String {
    var result = "List Launches:\n"
    this.forEach {
        result += "*${it.name}*\n ${it.netEpochTime?.let { netEpochTime -> timeUtils.toTime(netEpochTime) }}\n\n".toHTML()
    }
    return result
}