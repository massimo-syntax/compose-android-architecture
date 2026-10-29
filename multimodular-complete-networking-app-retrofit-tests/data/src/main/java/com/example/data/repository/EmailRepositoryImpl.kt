package com.example.data.repository

import com.example.core.networkresponse.error.Failure
import com.example.core.networkresponse.response.ResultEither
import com.example.data.mapper.EmailDetailsMapper
import com.example.data.mapper.EmailListMapper
import com.example.data.remote.api.ApiService
import com.example.data.remote.safecall.safeApiCall
import com.example.domain.model.emaildetails.EmailDetails
import com.example.domain.model.emaillist.EmailListItem
import com.example.domain.repository.EmailRepository
import javax.inject.Inject

class EmailRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val emailListMapper: EmailListMapper,
    private val emailDetailsMapper: EmailDetailsMapper
) : EmailRepository {

    override suspend fun getEmailList(): ResultEither<Failure, List<EmailListItem>> {
        return safeApiCall(
            apiCall = { apiService.getEmailList() },
            mapper = { emailListMapper.map(it) }
        )
    }

    override suspend fun getEmailDetail(): ResultEither<Failure, List<EmailDetails>> =
        safeApiCall(
            apiCall = { apiService.getEmailDetail() },
            mapper = { emailDetailsMapper.map(it) }
            //mapper = { dtos -> dtos.toDomain() }
        )

}
