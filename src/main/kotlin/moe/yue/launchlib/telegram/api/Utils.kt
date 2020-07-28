package moe.yue.launchlib.telegram.api

// Only add if the both strings are not null
infix fun String?.add(string: String?) = if (this.isNullOrEmpty() || string.isNullOrEmpty()) "" else "$this$string"

// Convert MarkdownV2 to HTML, special thanks to https://gist.github.com/jbroadway/2836900
fun String.toHTML() = this
    .replace(Regex("\\[([^\\[]+)]\\(([^\\)]+)\\)")) { "<a href='${it.groupValues[2]}'>${it.groupValues[1]}</a>" } // links
    .replace(Regex("(__)(.*?[^\\\\\\n])\\1")) { "<u>${it.groupValues[2].replace("\\_","_")}</u>" } // underline
    .replace(Regex("(\\*)(.*?[^\\\\\\n])\\1")) {"<b>${it.groupValues[2].replace("\\*","*")}</b>"} // bold
    .replace(Regex("(_)(.*?[^\\\\\\n])\\1")){"<i>${it.groupValues[2].replace("\\_","_")}</i>"} // italic
    .replace(Regex("\\~(.*?[^\\\\])\\~")) {"<s>${it.groupValues[1].replace("\\~","~")}</s>"} // strike
    .replace(Regex("\\`\\`\\`(.*\\n[\\s\\S]*?\\n)\\`\\`\\`")) {"<pre>${it.groupValues[1]}</pre>"} // code block
    .replace(Regex("(\\`)(.*?[^\\\\\\n])\\1")) {"<code>${it.groupValues[2].replace("\\`","`")}</code>"} // inline code
