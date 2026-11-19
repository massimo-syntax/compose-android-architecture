package com.example.db.entities

import com.example.db.tables.BlogsTable
import com.example.db.entities.UserEntity
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.r2dbc.ExperimentalR2dbcDaoApi
import org.jetbrains.exposed.v1.dao.r2dbc.ULongEntity
import org.jetbrains.exposed.v1.dao.r2dbc.ULongEntityClass


@OptIn(ExperimentalR2dbcDaoApi::class)
class BlogEntity(id: EntityID<ULong>) : ULongEntity(id) {
    companion object : ULongEntityClass<BlogEntity>(BlogsTable)

    var title by BlogsTable.title
    var content by BlogsTable.content
    val author by UserEntity referencedOn BlogsTable.author
}
