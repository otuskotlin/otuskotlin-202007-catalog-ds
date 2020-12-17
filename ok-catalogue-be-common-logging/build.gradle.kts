plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

repositories {
    mavenCentral()
}

val slf4jVersion: String by project
val logbackEncoderVersion: String by project

dependencies {
    implementation(kotlin("stdlib"))

    implementation("org.slf4j:slf4j-api:$slf4jVersion")
    implementation("net.logstash.logback:logstash-logback-encoder:$logbackEncoderVersion")
}
