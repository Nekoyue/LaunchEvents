import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.30-M1"
    kotlin("plugin.serialization") version "1.4.30-M1"
    id("com.github.johnrengelman.shadow") version "6.1.0"
    application
}
group = "moe.yue.launchlib"
version = "2.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
    maven { url = uri("https://dl.bintray.com/kotlin/ktor") }
    maven { url = uri("https://dl.bintray.com/kotlin/kotlin-eap") }
    maven { url = uri("https://dl.bintray.com/kotlin/exposed") }
}
dependencies {
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")

    // Http
    implementation("io.ktor:ktor-client-okhttp:1.5.0")
    implementation("io.ktor:ktor-client-serialization-jvm:1.5.0")
    implementation("io.ktor:ktor-client-logging-jvm:1.5.0")
    implementation("org.slf4j:slf4j-simple:1.7.30")
    implementation("io.github.microutils:kotlin-logging:2.0.4")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.1")

    // Database
    implementation("org.jetbrains.exposed:exposed-core:0.28.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.28.1")
    implementation("com.h2database:h2:1.4.200")

    // Config
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-hocon:1.0.1")

    testImplementation(kotlin("test-junit"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn" // Required by ConfigParser.kt
}
application {
    mainClassName = "moe.yue.launchlib.MainKt"
}
tasks.withType<ShadowJar> {
    manifest {
        attributes["Main-Class"] = "moe.yue.launchlib.MainKt"
    }
}