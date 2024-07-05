plugins {
    kotlin("jvm") version "1.9.23"
}

group = "kr.entropi.arki.lang"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    compileOnly("org.slf4j:slf4j-api:2.0.13")
}

kotlin {
    jvmToolchain(17)
}