package com.alejandra.amordepelis.features.user.data.datasources.remote.mapper

import com.alejandra.amordepelis.features.user.data.datasources.remote.model.UserProfileDto
import com.alejandra.amordepelis.features.user.domain.entities.PartnerProfile
import com.alejandra.amordepelis.features.user.domain.entities.UserProfile

fun UserProfileDto.toDomain(roomName: String? = null, ownInviteCode: String? = null): UserProfile {
    return UserProfile(
        id = id.toString(),
        username = username ?: "",
        email = email,
        passwordMasked = passwordMasked ?: "********",
        partner = partner?.let {
            PartnerProfile(
                id = it.id,
                username = it.username,
                email = it.email
            )
        },
        roomName = roomName,
        ownInviteCode = ownInviteCode,
        role = role ?: ""
    )
}
