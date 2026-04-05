package com.alejandra.amordepelis.features.user.data.datasources.remote.mapper

import com.alejandra.amordepelis.features.user.data.datasources.remote.model.UserSearchResultDto
import com.alejandra.amordepelis.features.user.domain.entities.UserSearchResult

fun UserSearchResultDto.toDomain(): UserSearchResult {
    return UserSearchResult(
        id = id,
        username = username,
        email = email
    )
}
