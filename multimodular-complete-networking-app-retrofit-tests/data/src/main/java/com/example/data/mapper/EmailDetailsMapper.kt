package com.example.data.mapper

import com.example.core.networkresponse.mapper.ResultMapper
import com.example.data.model.dto.emaildetails.EmailDetailsDto
import com.example.data.model.dto.emaildetails.PayloadDto
import com.example.domain.model.emaildetails.EmailDetails
import com.example.domain.model.emaildetails.Payload
import javax.inject.Inject

class EmailDetailsMapper @Inject constructor() :
    ResultMapper<List<EmailDetailsDto>, List<EmailDetails>>
{
    override fun map(input: List<EmailDetailsDto>): List<EmailDetails> =
        input.filter {
            it.id != null
        }.map {
            it.toDomain()
        }

    private fun EmailDetailsDto.toDomain() =
        EmailDetails(
            id = id ?: "404",
            body = body.orEmpty(),
            image = image.orEmpty(),
            payload = payload?.toDomain(),
            title = title.orEmpty()
        )

    private fun PayloadDto.toDomain() =
        Payload(
            email = email.orEmpty(),
            from = from.orEmpty(),
            timestamp = timestamp ?: 0L
        )
}
