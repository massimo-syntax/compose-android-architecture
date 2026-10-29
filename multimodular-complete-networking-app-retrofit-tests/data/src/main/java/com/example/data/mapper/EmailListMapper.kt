package com.example.data.mapper

import com.example.core.networkresponse.mapper.ResultMapper
import com.example.core.networkresponse.orDefault
import com.example.data.model.dto.emaillist.EmailListItemDto
import com.example.domain.model.emaillist.EmailListItem
import javax.inject.Inject

class EmailListMapper @Inject constructor() :
    ResultMapper<List<EmailListItemDto>, List<EmailListItem>>
{
    override fun map(input: List<EmailListItemDto>): List<EmailListItem> =
        input.filter {
            it.id != null && it.from != null
        }.map {
            it.toDomain()
        }.sortedBy {
            it.timestamp
        }.reversed()

    private fun EmailListItemDto.toDomain() =
        EmailListItem(
            id = id!!,
            title = title.orEmpty(),
            from = from ?: "Deleted Account",
            image = image.orEmpty(),
            timestamp = timestamp ?: 0L,
            favorite = favorite.orDefault(false)
        )
}