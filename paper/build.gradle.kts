plugins {
    kotlin("jvm") version "1.9.23"

    id("io.papermc.paperweight.userdev") version "1.7.1"
}

group = "kr.entropi.arki.lang"
version = "1.0-SNAPSHOT"

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")
    maven("https://jitpack.io")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    testImplementation(kotlin("test"))

    compileOnly("org.slf4j:slf4j-api:2.0.13")

    implementation(project(":core"))

    // Paper
    paperweight.paperDevBundle("1.20.4-R0.1-SNAPSHOT")
}

kotlin {
    jvmToolchain(17)
}