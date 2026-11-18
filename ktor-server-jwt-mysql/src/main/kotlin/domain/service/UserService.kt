package com.example.domain.service

import com.example.domain.model.User
import com.example.domain.model.UserAuthRequest

interface UserService {
    suspend fun registerUser(user: UserAuthRequest): User?
    suspend fun loginUser(user: UserAuthRequest): User?
    suspend fun getUserById(id: Long): User?
}