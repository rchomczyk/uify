plugins {
    `java-library`
    `maven-publish`
}

group = "dev.shiza"
version = "2.0.0"

publishing {
    repositories {
        mavenLocal()
        maven("aurora-repo", "https://maven.blockgate.finance/auroramc", "auroraUsername", "auroraPassword")
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

fun RepositoryHandler.maven(
    name: String,
    url: String,
    username: String,
    password: String,
    snapshots: Boolean = true
) {
    val isSnapshot = version.toString().endsWith("-SNAPSHOT")
    if (isSnapshot && !snapshots) {
        return
    }

    this.maven {
        this.name = name
        this.url = uri(url)
        this.credentials {
            this.username = findProperty(username) as String
            this.password = findProperty(password) as String
        }
    }
}