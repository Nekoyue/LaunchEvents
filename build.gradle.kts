import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"

    // Run ./gradlew dependencyUpdates to check for dependency updates
    id("com.github.ben-manes.versions") version "0.40.0"
    application
}
group = "moe.yue.launchlib"
version = "2.1-SNAPSHOT"

repositories {
    mavenCentral()
}
dependencies {
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")

    // Network
    implementation("io.ktor:ktor-client-okhttp:1.6.7")
    implementation("io.ktor:ktor-client-serialization-jvm:1.6.7")
    implementation("io.ktor:ktor-client-logging-jvm:1.6.7")
    implementation("org.slf4j:slf4j-simple:1.7.32")
    implementation("io.github.microutils:kotlin-logging:2.1.21")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.2")

    // Database
    implementation("org.jetbrains.exposed:exposed-core:0.37.3")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.37.3")
    implementation("com.h2database:h2:2.0.204")

    // Config
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-hocon:1.3.2")

    testImplementation(kotlin("test-junit"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn" // Required by ConfigParser.kt
}
application {
     mainClass.set("moe.yue.launchlib.MainKt")
}
tasks.withType<ShadowJar> {
    manifest {
        attributes["Main-Class"] = "moe.yue.launchlib.MainKt"
    }
}