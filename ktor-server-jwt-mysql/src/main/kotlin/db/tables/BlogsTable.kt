package com.example.db.tables

import com.example.db.tables.UsersTable
import org.jetbrains.exposed.v1.core.dao.id.ULongIdTable
import org.jetbrains.exposed.v1.core.ReferenceOption

object BlogsTable : ULongIdTable() {
    val title = varchar("title", length = 200)
    val content = text("content")
    val author = reference(
        "user_id",
        UsersTable.id,
        onDelete = ReferenceOption.CASCADE
    )
}
