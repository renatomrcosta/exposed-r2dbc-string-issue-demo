package com.xunfos

import kotlinx.coroutines.flow.single
import org.jetbrains.exposed.v1.r2dbc.insert
import org.jetbrains.exposed.v1.r2dbc.selectAll
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.r2dbc.update
import org.testcontainers.containers.PostgreSQLContainer


suspend fun main() {
    val container = PostgreSQLContainer("postgres:16-alpine")
    container.withContainerRuntime { db ->
        doMigrations()

        runCatching<Unit> { doNullStringTest() }.getOrElse {
            println("Error running string test: $it")
            println("${it.stackTrace.toList()}")
        }

        runCatching { doEnumerationTest() }.getOrElse {
            println("Error running enumeration test: $it")
            println("${it.stackTrace.toList()}")
        }
    }
}


suspend fun doNullStringTest() {
    suspendTransaction {
        // Insert a row with a nullable value in the nullable string
        PersonTable.insert {
            it[name] = "Some name"
            it[optionalDescription] = "blaaaa"
        }

        // Gets value
        val row = PersonTable.selectAll().single()
        println("Row was fetched ok! - ${row[PersonTable.name]} ${row[PersonTable.optionalDescription]}")
    }

    suspendTransaction {
        PersonTable.update {
            it[optionalDescription] = null
        }

        // Gets value
        val row = PersonTable.selectAll().single()
        println("Row was fetched ok! - ${row[PersonTable.name]} ${row[PersonTable.optionalDescription]}")
    }
}

suspend fun doEnumerationTest() {
    suspendTransaction {
        // Inserts the value
        CarTable.insert {
            it[CarTable.model] = "Some car"
            it[CarTable.color] = Color.RED
        }

        // Gets the value
        val row = CarTable.selectAll().single()
        println("Row was fetched ok! - ${row[CarTable.model]} ${row[CarTable.color]}")
    }
    suspendTransaction {
        CarTable.update {
            it[CarTable.color] = null
        }

        val row2 = CarTable.selectAll().single()
        println("Row was fetched ok! - ${row2[CarTable.model]} ${row2[CarTable.color]}")
    }
}
