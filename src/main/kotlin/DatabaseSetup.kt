package com.xunfos

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.r2dbc.SchemaUtils
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction

object PersonTable : IntIdTable("person") {
    val name = text("name")
    val optionalDescription = text("description").nullable()
}

enum class Color { RED, GREEN, BLUE }

object CarTable : IntIdTable("car") {
    val model = text("model")
    val color = enumerationByName("color", 8, Color::class).nullable()
}

suspend fun doMigrations() {
    suspendTransaction {
        SchemaUtils.create(CarTable, PersonTable)
    }
}