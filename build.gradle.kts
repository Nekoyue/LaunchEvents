import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.4.0-rc"
    kotlin("plugin.serialization") version "1.4.0-rc"
    id("com.github.johnrengelman.shadow") version "6.0.0"
    application
}
group = "moe.yue.launchlib"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
    maven { url = uri("https://dl.bintray.com/kotlin/ktor") }
    maven { url = uri("https://dl.bintray.com/kotlin/kotlin-eap") }
    maven { url = uri("https://dl.bintray.com/kotlin/exposed") }
}
dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.8-1.4.0-rc")

    // Http
    implementation("io.ktor:ktor-client-okhttp:1.3.2-1.4.0-rc")
    implementation("io.ktor:ktor-client-serialization-jvm:1.3.2-1.4.0-rc")
    implementation("io.ktor:ktor-client-logging-jvm:1.3.2-1.4.0-rc")
    implementation("org.slf4j:slf4j-simple:1.7.30")
    implementation("io.github.microutils:kotlin-logging:1.8.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:1.0-M1-1.4.0-rc")

    // Database
    implementation("org.jetbrains.exposed:exposed-core:0.26.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.26.1")
    implementation("com.h2database:h2:1.4.200")

    // For parsing .kts scripts, i.e. config.kts file
    implementation(kotlin("script-runtime"))
    implementation(kotlin("script-util"))
    implementation(kotlin("compiler-embeddable"))
    implementation(kotlin("scripting-compiler-embeddable"))
    implementation("net.java.dev.jna:jna:5.6.0")

    testImplementation(kotlin("test-junit"))
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
application {
    mainClassName = "moe.yue.launchlib.MainKt"
}

tasks.withType<ShadowJar> {
    minimize()
    manifest {
        attributes["Main-Class"] = "moe.yue.launchlib.MainKt"
    }
}