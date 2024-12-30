rootProject.name = "uify"
include(":uify-common")
include(":uify-nms:uify-nms-common")
include(":uify-nms:uify-nms-v1_21_4")
include(":uify-nms:uify-nms-v1_20_4")
include(":uify-nms:uify-nms-adapter")

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}
