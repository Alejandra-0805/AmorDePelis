package com.alejandra.amordepelis.features.lists.data.datasources.remote.mapper

import com.alejandra.amordepelis.features.lists.data.datasources.remote.model.CreateListRequestDto
import com.alejandra.amordepelis.features.lists.domain.entities.CreateListParams

fun CreateListParams.toDto(): CreateListRequestDto {
    return CreateListRequestDto(
        name = name,
        description = description,
        colorHex = colorHex
    )
}
