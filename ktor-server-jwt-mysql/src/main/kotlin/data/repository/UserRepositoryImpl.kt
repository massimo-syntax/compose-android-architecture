package com.example.data.repository

import com.example.db.entities.UserEntity
import com.example.db.tables.UsersTable
import com.example.db.utils.dbQuery
import com.example.domain.model.User
import com.example.domain.model.UserAuthRequest
import com.example.domain.repository.UserRepository
import kotlinx.coroutines.flow.firstOrNull
import org.jetbrains.exposed.v1.core.eq
import org.mindrot.jbcrypt.BCrypt

class UserRepositoryImpl : UserRepository {
    override suspend fun createUser(userRequest: UserAuthRequest): User? = dbQuery {
        val entity = UserEntity.find { UsersTable.name eq userRequest.username }.firstOrNull()
        entity?.toUser()
            ?: UserEntity.newSuspend {
                name = userRequest.username
                password = hashPassword(userRequest.password)
            }.toUser()
    }

    override suspend fun getUserById(id: Long): User? = dbQuery {
        UserEntity.findById(id.toULong())?.toUser()
    }

    override suspend fun authenticate(userRequest: UserAuthRequest): User? = dbQuery{
        val entity = UserEntity.find { UsersTable.name eq userRequest.username }.firstOrNull()
        entity?.takeIf {
            isPasswordHash(userRequest.password, it.password)
        }?.toUser()
    }

    override fun hashPassword(password: String): String =
        BCrypt.hashpw(password, BCrypt.gensalt())


    override fun isPasswordHash(plainText: String, hashed: String): Boolean =
        BCrypt.checkpw(plainText, hashed)

    override suspend fun updateUser(user: User): User? = dbQuery {
        UserEntity.findByIdAndUpdate(user.id.toULong()) {
            it.name = user.name
        }?.toUser()
    }

    override suspend fun deleteUser(id: Long): Boolean {
        val entity = dbQuery {
            UserEntity.findById(id.toULong())
        }?.also { it.delete() }
        return entity != null
    }

}