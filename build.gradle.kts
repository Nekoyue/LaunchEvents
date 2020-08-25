import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.0"
    kotlin("plugin.serialization") version "1.4.0"
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
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")

    // Http
    implementation("io.ktor:ktor-client-okhttp:1.4.0")
    implementation("io.ktor:ktor-client-serialization-jvm:1.4.0")
    implementation("io.ktor:ktor-client-logging-jvm:1.4.0")
    implementation("org.slf4j:slf4j-simple:1.7.30")
    implementation("io.github.microutils:kotlin-logging:1.8.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.0-RC")

    // Database
    implementation("org.jetbrains.exposed:exposed-core:0.26.2")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.26.2")
    implementation("com.h2database:h2:1.4.200")

    // Config
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-hocon:1.0.0-RC")

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