package com.example.data.mapper

import com.example.data.model.dto.emaildetails.EmailDetailsDto
import com.example.data.model.dto.emaildetails.PayloadDto
import com.example.domain.model.emaildetails.EmailDetails
import com.example.domain.model.emaildetails.Payload

fun PayloadDto.toDomain(): Payload =
    Payload(
        email = email.orEmpty(),
        from = from.orEmpty(),
        timestamp = timestamp ?: 0L
    )

fun EmailDetailsDto.toDomain(): EmailDetails =
    EmailDetails(
        id = id ?: "404",
        body = body.orEmpty(),
        image = image.orEmpty(),
        payload = payload?.toDomain(),
        title = title.orEmpty()
    )

fun List<EmailDetailsDto>.toDomain(): List<EmailDetails> = map { it.toDomain() }
