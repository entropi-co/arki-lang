plugins {
    kotlin("jvm") version "1.9.23" apply false
    `maven-publish`
}

group = "kr.entropi.arki.lang"
version = "1.0-SNAPSHOT"

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "maven-publish")

    repositories {
        mavenLocal()
        mavenCentral()
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