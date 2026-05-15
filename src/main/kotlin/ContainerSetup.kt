package com.xunfos

import io.r2dbc.spi.ConnectionFactoryOptions
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabaseConfig
import org.testcontainers.containers.PostgreSQLContainer

suspend fun <T> PostgreSQLContainer<*>.withContainerRuntime(block: suspend (R2dbcDatabase) -> T): T {
    return try {
        this.start()
        println("Container Started")
        val url = "r2dbc:postgresql://${this.host}:${this.getMappedPort(5432)}/${this.databaseName}"
        val properties = ConnectionProperties(
            url = url,
            username = this.username,
            password = this.password
        )
        val database = createR2DBCDatabaseConnection(properties)
        println("R2DBC Connection Successful: $url")
        block(database)
    } finally {
        println("Stopping container")
        this.stop()
    }
}

data class ConnectionProperties(
    val url: String,
    val username: String,
    val password: String,
)

fun createR2DBCDatabaseConnection(
    connectionProperties: ConnectionProperties,
): R2dbcDatabase = with(connectionProperties) {
    R2dbcDatabase.connect(
        url = url,
        databaseConfig = R2dbcDatabaseConfig {
            defaultMaxAttempts = 1
            connectionFactoryOptions {
                option(ConnectionFactoryOptions.USER, username)
                option(ConnectionFactoryOptions.PASSWORD, password)
            }
        }
    )
}