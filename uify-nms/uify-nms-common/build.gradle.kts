plugins {
    `uify-java`
    `uify-publish`
    `uify-repositories`
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
}

uifyPublish {
    artifactId = "uify-nms-common"
}