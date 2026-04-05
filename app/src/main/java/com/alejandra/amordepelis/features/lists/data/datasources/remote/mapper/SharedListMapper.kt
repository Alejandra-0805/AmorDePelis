package com.alejandra.amordepelis.features.lists.data.datasources.remote.mapper

import com.alejandra.amordepelis.features.lists.data.datasources.remote.model.AnnouncementDto
import com.alejandra.amordepelis.features.lists.data.datasources.remote.model.SharedListDetailsDto
import com.alejandra.amordepelis.features.lists.data.datasources.remote.model.SharedListDto
import com.alejandra.amordepelis.features.lists.domain.entities.Announcement
import com.alejandra.amordepelis.features.lists.domain.entities.SharedList
import com.alejandra.amordepelis.features.lists.domain.entities.SharedListDetails
import com.alejandra.amordepelis.features.movies.data.datasources.remote.mapper.toDomain

fun SharedListDto.toDomain(): SharedList {
    return SharedList(
        id = id,
        name = name,
        description = description,
        colorHex = colorHex,
        movieCount = movieCount
    )
}

fun SharedListDetailsDto.toDomain(): SharedListDetails {
    return SharedListDetails(
        id = id,
        name = name,
        description = description,
        colorHex = colorHex,
        movies = movies.map { it.toDomain() }
    )
}

fun AnnouncementDto.toDomain(): Announcement {
    return Announcement(
        id = id,
        title = title,
        description = description,
        imageUrl = imageUrl
    )
}

fun List<AnnouncementDto>.toAnnouncementDomainList(): List<Announcement> = map { it.toDomain() }
