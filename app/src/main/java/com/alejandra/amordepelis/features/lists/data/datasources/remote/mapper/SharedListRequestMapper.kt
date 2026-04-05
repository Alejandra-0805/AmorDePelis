package com.alejandra.amordepelis.features.lists.data.datasources.remote.mapper

import com.alejandra.amordepelis.features.lists.data.datasources.remote.model.CreateListRequestDto
import com.alejandra.amordepelis.features.lists.data.datasources.remote.model.UpdateListRequestDto
import com.alejandra.amordepelis.features.lists.domain.entities.CreateListParams
import com.alejandra.amordepelis.features.lists.domain.entities.UpdateListParams

fun CreateListParams.toDto(): CreateListRequestDto {
    return CreateListRequestDto(
        name = name,
        description = description,
        colorHex = colorHex
    )
}

fun UpdateListParams.toDto(): UpdateListRequestDto {
    return UpdateListRequestDto(
        listId = listId,
        name = name,
        description = description
    )
}
