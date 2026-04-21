package com.alejandra.amordepelis.features.lists.data.datasources.local.mapper

import com.alejandra.amordepelis.core.database.entities.ListEntity
import com.alejandra.amordepelis.features.lists.data.datasources.remote.model.CustomListResponseDto


fun CustomListResponseDto.toEntity(roomId: Int = 0): ListEntity {
    return ListEntity(
        id = id,
        name = name,
        description = "",
        sala_id = roomId
    )
}

fun List<CustomListResponseDto>.toEntityList(roomId: Int = 0): List<ListEntity> {
    return this.map { it.toEntity(roomId) }
}
