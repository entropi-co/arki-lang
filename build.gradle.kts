plugins {
    kotlin("jvm") version "1.9.23"
}

group = "kr.entropi.arki.lang"
version = "1.0-SNAPSHOT"

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        mavenLocal()
        mavenCentral()
    }

    dependencies {
        compileOnly("org.slf4j:slf4j-api:2.0.13")
    }
}