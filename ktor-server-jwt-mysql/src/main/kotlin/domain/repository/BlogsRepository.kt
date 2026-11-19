package com.example.domain.repository

import com.example.domain.model.Blog
import com.example.domain.model.BlogRequest

interface BlogsRepository {

    suspend fun createBlog(userId: Long, request: BlogRequest): Blog
    suspend fun getAll(): List<Blog>

}
