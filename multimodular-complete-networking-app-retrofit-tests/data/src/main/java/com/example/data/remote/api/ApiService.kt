package com.example.data.remote.api

import com.example.data.model.dto.emaildetails.EmailDetailsDto
import com.example.data.model.dto.emaillist.EmailListItemDto
import retrofit2.http.GET

interface ApiService {
    @GET("api/v1/emaillist")
    suspend fun getEmailList(): List<EmailListItemDto>

    @GET("api/v1/emaildetails")
    suspend fun getEmailDetail(): List<EmailDetailsDto>
}