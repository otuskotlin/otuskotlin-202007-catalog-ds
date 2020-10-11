rootProject.name = "ok-202007-catalogue"

pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        val bmuschkoVersion: String by settings

        kotlin("multiplatform") version kotlinVersion apply false
        kotlin("jvm") version kotlinVersion apply false
        kotlin("plugin.serialization") version kotlinVersion apply false
        id("com.bmuschko.docker-java-application") version bmuschkoVersion
    }
}

include("ok-catalogue-backend-common")
include("ok-catalogue-transport-common")
include("ok-catalogue-transport-rest")
include("ok-catalogue-app-ktor")
include("ok-catalogue-be-logic")
include("ok-catalogue-be-handlers")
include("ok-catalogue-be-validator")
