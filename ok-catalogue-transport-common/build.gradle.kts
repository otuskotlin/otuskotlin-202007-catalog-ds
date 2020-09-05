plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

repositories {
    mavenCentral()
}

val serializationVersion: String by project

dependencies {
    implementation(kotlin("stdlib"))
    //implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serializationVersion")
    api("org.jetbrains.kotlinx:kotlinx-serialization-core:$serializationVersion")
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}
