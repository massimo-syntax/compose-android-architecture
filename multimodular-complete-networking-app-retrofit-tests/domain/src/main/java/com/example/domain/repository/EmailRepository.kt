package com.example.domain.repository

import com.example.core.networkresponse.error.Failure
import com.example.core.networkresponse.response.ResultEither
import com.example.domain.model.emaildetails.EmailDetails
import com.example.domain.model.emaillist.EmailListItem

interface EmailRepository {
    suspend fun getEmailList(): ResultEither<Failure, List<EmailListItem>>
    suspend fun getEmailDetail(): ResultEither<Failure, List<EmailDetails>>
}
