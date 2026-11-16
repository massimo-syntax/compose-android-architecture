package com.example.domain.repository

import com.example.domain.model.User
import com.example.domain.model.UserAuthRequest

interface UserRepository {

    suspend fun createUser(userRequest: UserAuthRequest): User?
    suspend fun getUserById(id: Long): User?
    suspend fun updateUser(user: User): User?
    suspend fun deleteUser(id: Long): Boolean
    suspend fun authenticate(userRequest: UserAuthRequest): User?
    fun hashPassword(password: String): String
    fun isPasswordHash(plainText: String, hashed: String): Boolean

}