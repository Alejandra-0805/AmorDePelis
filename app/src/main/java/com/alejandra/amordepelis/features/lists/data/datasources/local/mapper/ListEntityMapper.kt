package com.alejandra.amordepelis.features.lists.data.datasources.local.mapper

import com.alejandra.amordepelis.core.database.entities.ListEntity
import com.alejandra.amordepelis.features.lists.domain.entities.SharedList


fun ListEntity.toDomain(): SharedList {
    return SharedList(
        id = id.toString(),
        name = name
    )
}

fun List<ListEntity>.toDomainList(): List<SharedList> {
    return this.map { it.toDomain() }
}
