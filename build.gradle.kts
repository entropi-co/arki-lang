plugins {
    kotlin("jvm") version "1.9.23"
    `maven-publish`
}

group = "kr.entropi.arki.lang"
version = "1.0-SNAPSHOT"

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "maven-publish")

    repositories {
        mavenLocal()
        mavenCentral()
    }

    dependencies {
        compileOnly("org.slf4j:slf4j-api:2.0.13")
    }

    configure<PublishingExtension> {
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/entropi-kr/arki-lang")
                credentials {
                    username = System.getenv("GITHUB_ACTOR")
                    password = System.getenv("GITHUB_TOKEN")
                }
            }
        }

        publications {
            register<MavenPublication>("mavenKt") {
                from(components["java"])
            }
        }
    }
}