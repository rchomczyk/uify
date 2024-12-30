plugins {
    `uify-java`
    `uify-publish`
    `uify-repositories`
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.8"
}

dependencies {
    api(project(":uify-nms:uify-nms-common"))
    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")
}

uifyPublish {
    artifactId = "uify-nms-v1_21_4"
}