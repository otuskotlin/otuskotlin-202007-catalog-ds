plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":ok-catalogue-be-common"))
    implementation(project(":ok-catalogue-transport-common"))

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}
