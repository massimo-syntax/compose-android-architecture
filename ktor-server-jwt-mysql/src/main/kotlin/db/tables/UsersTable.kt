package com.example.db.tables

import org.jetbrains.exposed.v1.core.dao.id.ULongIdTable

object UsersTable : ULongIdTable() {
    val name = varchar("name", length = 50)

    // Value can't be stored to database column because exceeds length (60 > 50)
    // password hash is 60, now we know
    val password = varchar("password", 256)
}