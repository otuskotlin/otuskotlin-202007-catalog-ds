package ru.otus.otuskotlin.configs

import com.typesafe.config.Config
import io.ktor.application.*
import io.ktor.config.*
import io.ktor.features.*
import io.ktor.util.*

data class CassandraConfig(
        val hosts: String = "localhost",
        val port: Int = 9042,
        val user: String = "cassandra",
        val pass: String = "cassandra",
        val keyspace: String = "test_keyspace"
) {
    @KtorExperimentalAPI
    constructor(config: ApplicationConfig): this(
            hosts = config.property("$PATH.hosts").getString(),
            port = config.property("$PATH.port").getString().toInt(),
            user = config.property("$PATH.user").getString(),
            pass = config.property("$PATH.pass").getString(),
            keyspace = config.property("$PATH.keyspace").getString()
    )

    companion object {
        const val PATH = "ktor.repository.cassandra"
    }
}