package moe.yue.launchlib

import java.io.File
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager

data class Config(
    val telegramToken: String,
    val telegramChannelId: Long,
    val telegramAdminId: Long
)

lateinit var config: Config

fun loadConfig() {
    val configFile: String =
        File("config.kts").takeIf { it.canRead() }?.readText()
            ?: File("config.kts").apply { extractConfig() }.readText()
    config = evaluateScript(configFile)
}

// Extract config file from Jar
private fun extractConfig() {
    val rawConfig = Thread.currentThread().contextClassLoader.getResource("config.kts")!!.readBytes()
    File("config.kts").writeBytes(rawConfig)
}

// Parse and cast config script
private fun evaluateScript(script: String): Config {
    val engine: ScriptEngine =
        ScriptEngineManager(Thread.currentThread().contextClassLoader).getEngineByExtension("kts")

    return runCatching { engine.eval(script) }.getOrElse { throw Exception("Failed to load config.kts", it) }
        .takeIf { it is Config }?.let { it as Config }
        ?: throw ClassCastException("Failed to parse config, check the format...")
}