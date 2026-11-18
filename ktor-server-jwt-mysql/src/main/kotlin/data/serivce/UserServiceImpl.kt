package com.example.data.serivce

import com.example.domain.model.User
import com.example.domain.model.UserAuthRequest
import com.example.domain.repository.UserRepository
import com.example.domain.service.UserService

class UserServiceImpl(
    private val userRepository: UserRepository,
) : UserService {
    override suspend fun registerUser(user: UserAuthRequest) =
        // validate password...
        userRepository.createUser(user)

    override suspend fun loginUser(user: UserAuthRequest) =
        // validation first...
        userRepository.authenticate(user)

    override suspend fun getUserById(id: Long): User? =
        userRepository.getUserById(id)
}