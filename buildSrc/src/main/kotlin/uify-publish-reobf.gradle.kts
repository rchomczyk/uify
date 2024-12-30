plugins {
    `java-library`
    `maven-publish`
}

group = "dev.shiza"
version = "1.0.0-SNAPSHOT"

publishing {
    repositories {
        mavenLocal()
    }
}

interface UifyPublishExtension {
    var artifactId: String
}

val extension = extensions.create<UifyPublishExtension>("uifyPublish")

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                artifactId = extension.artifactId
                artifact(tasks.named("reobfJar")) {
                    builtBy(tasks.named("reobfJar"))
                }
            }
        }
    }
}