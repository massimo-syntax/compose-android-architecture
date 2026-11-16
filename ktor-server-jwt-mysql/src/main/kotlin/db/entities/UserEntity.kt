package com.example.db.entities

import com.example.db.tables.UsersTable
import com.example.domain.model.User
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.r2dbc.ExperimentalR2dbcDaoApi
import org.jetbrains.exposed.v1.dao.r2dbc.ULongEntity
import org.jetbrains.exposed.v1.dao.r2dbc.ULongEntityClass



@OptIn(ExperimentalR2dbcDaoApi::class)
class UserEntity(id: EntityID<ULong>) : ULongEntity(id) {
    companion object : ULongEntityClass<UserEntity>(UsersTable)

    var name by UsersTable.name
    var password by UsersTable.password

    fun toUser() = User(
        id = id.value.toLong(),
        name = name,
    )
}
