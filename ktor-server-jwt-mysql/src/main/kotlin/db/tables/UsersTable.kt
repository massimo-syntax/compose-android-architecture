package com.example.db.tables

import org.jetbrains.exposed.v1.core.dao.id.ULongIdTable

object UsersTable : ULongIdTable() {
    val name = varchar("name", length = 50)
    val password = varchar("password", 256)
}
