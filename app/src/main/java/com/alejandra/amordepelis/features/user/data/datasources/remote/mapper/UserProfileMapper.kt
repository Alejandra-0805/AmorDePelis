package com.alejandra.amordepelis.features.user.data.datasources.remote.mapper

import com.alejandra.amordepelis.features.user.data.datasources.remote.model.UserProfileDto
import com.alejandra.amordepelis.features.user.domain.entities.PartnerProfile
import com.alejandra.amordepelis.features.user.domain.entities.UserProfile

fun UserProfileDto.toDomain(): UserProfile {
    return UserProfile(
        id = id,
        username = username,
        email = email,
        passwordMasked = passwordMasked,
        partner = partner?.let {
            PartnerProfile(
                id = it.id,
                username = it.username,
                email = it.email
            )
        }
    )
}
