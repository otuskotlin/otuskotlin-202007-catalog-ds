plugins {
    kotlin("jvm") version "1.3.72"
}

group = "ru.otus.otuskotlin.catalogue"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}
