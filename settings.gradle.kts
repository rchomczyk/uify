rootProject.name = "uify"
include(":uify-common")
include(":uify-nms:uify-nms-common")
include(":uify-nms:uify-nms-v1_21_4")
include(":uify-nms:uify-nms-v1_20_4")
include(":uify-nms:uify-nms-adapter")

// Include only in case if you want to launch the demo
include(":uify-demo")

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}