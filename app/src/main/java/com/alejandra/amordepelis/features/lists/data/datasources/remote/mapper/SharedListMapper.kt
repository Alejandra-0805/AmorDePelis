package com.alejandra.amordepelis.features.lists.data.datasources.remote.mapper

import com.alejandra.amordepelis.features.lists.data.datasources.remote.model.CustomListResponseDto
import com.alejandra.amordepelis.features.lists.data.datasources.remote.model.SharedListDetailsDto
import com.alejandra.amordepelis.features.lists.domain.entities.SharedList
import com.alejandra.amordepelis.features.lists.domain.entities.SharedListDetails
import com.alejandra.amordepelis.features.movies.data.datasources.remote.mapper.toDomain
import com.alejandra.amordepelis.features.movies.data.datasources.remote.model.MovieDto

fun CustomListResponseDto.toDomain(): SharedList {
    return SharedList(
        id = id.toString(),
        name = name
    )
}

fun SharedListDetailsDto.toDomain(): SharedListDetails {
    return SharedListDetails(
        id = id.toString(),
        name = name,
        description = description,
        colorHex = colorHex,
        movies = movies.map { it.toDomain() }
    )
}

fun List<MovieDto>.toSharedListDetails(listId: String, listName: String): SharedListDetails {
    return SharedListDetails(
        id = listId,
        name = listName,
        movies = this.map { it.toDomain() }
    )
}
