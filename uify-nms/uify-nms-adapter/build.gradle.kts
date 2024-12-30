plugins {
    `uify-java`
    `uify-publish`
    `uify-repositories`
}

dependencies {
    api(project(":uify-nms:uify-nms-common"))
    api(project(":uify-nms:uify-nms-v1_20_4"))
    api(project(":uify-nms:uify-nms-v1_21_4"))
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
}

uifyPublish {
    artifactId = "uify-nms-adapter"
}