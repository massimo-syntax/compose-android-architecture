package com.example.data.repository

import com.example.db.entities.BlogEntity
import com.example.db.entities.UserEntity
import com.example.db.utils.dbQuery
import com.example.domain.model.Blog
import com.example.domain.model.BlogRequest
import com.example.domain.repository.BlogsRepository
import kotlinx.coroutines.flow.toList
import org.jetbrains.exposed.v1.dao.r2dbc.ExperimentalR2dbcDaoApi

@OptIn(ExperimentalR2dbcDaoApi::class)
class BlogsRepositoryImpl : BlogsRepository {

    override suspend fun createBlog(userId: Long, request: BlogRequest): Blog = dbQuery {
        val user = UserEntity.findById(userId.toULong())
            ?: throw IllegalArgumentException("User with id $userId not found")

        val blogEntity = BlogEntity.newSuspend {
            title = request.title
            content = request.content
            author.set(user)
        }

        Blog(
            id = blogEntity.id.value.toLong(),
            title = blogEntity.title,
            content = blogEntity.content,
            author = blogEntity.author().toUser()
        )
    } ?: throw IllegalStateException("Failed to create blog")

    override suspend fun getAll(): List<Blog> = dbQuery {
        BlogEntity.all().toList().map { entity ->
            Blog(
                id = entity.id.value.toLong(),
                title = entity.title,
                content = entity.content,
                author = entity.author().toUser()
            )
        }
    } ?: emptyList()
}
