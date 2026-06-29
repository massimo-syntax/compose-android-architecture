package com.example.data.mapper

import com.example.data_local.entity.ScienceEntity
import com.example.data_remote.dto.ScienceDto
import com.example.domain.model.Science

fun ScienceDto.toDomain(): Science =
    Science(
        id = id,
        field = field,
        concept = concept,
        researcher = researcher,
    )

fun ScienceEntity.toDomain(): Science =
    Science(id,field,concept,researcher)

fun ScienceDto.toEntity(): ScienceEntity =
    ScienceEntity(id,field,concept,researcher)