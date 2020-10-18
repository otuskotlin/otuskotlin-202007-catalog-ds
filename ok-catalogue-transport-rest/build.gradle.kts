val logbackVersion: String by project

plugins {
    kotlin("jvm")

}

group = rootProject.group
version = rootProject.version

repositories {
    mavenCentral()
}

dependencies {
    val slf4jVersion: String by project
    val coroutinesVersion: String by project


    implementation("org.slf4j:slf4j-api:$slf4jVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    implementation(kotlin("stdlib"))
    implementation(project(":ok-catalogue-backend-common"))
    implementation(project(":ok-catalogue-transport-common"))
    implementation(project(":ok-catalogue-be-logic"))

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}
