pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

fun module(name: String) {
    include(name)
    project(":${name}").name = "${rootProject.name}-${name}"
}

rootProject.name = "commons"

module("inject")
module("reflection")
module("sql")
