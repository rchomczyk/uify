plugins {
    `uify-java`
    `uify-publish-reobf`
    `uify-repositories`
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.8"
}

dependencies {
    api(project(":uify-nms:uify-nms-common"))
    paperweight.paperDevBundle("1.20.4-R0.1-SNAPSHOT")
}

uifyPublish {
    artifactId = "uify-nms-v1_20_4"
}