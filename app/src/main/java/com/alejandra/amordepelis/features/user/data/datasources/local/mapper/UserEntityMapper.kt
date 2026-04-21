package com.alejandra.amordepelis.features.user.data.datasources.local.mapper

import com.alejandra.amordepelis.core.database.entities.UserEntity
import com.alejandra.amordepelis.features.user.domain.entities.PartnerProfile
import com.alejandra.amordepelis.features.user.domain.entities.UserProfile

fun UserEntity.toDomain(): UserProfile = UserProfile(
    id            = id.toString(),
    username      = username,
    email         = email,
    passwordMasked = passwordMasked,
    roomName      = roomName,
    ownInviteCode = ownInviteCode,
    partner       = if (hasPartner && !partnerUsername.isNullOrBlank()) {
        PartnerProfile(
            id       = "cached",
            username = partnerUsername,
            email    = partnerEmail ?: ""
        )
    } else null,
    role = role
)

fun UserProfile.toEntity(): UserEntity = UserEntity(
    id            = id.toIntOrNull() ?: 0,
    username      = username,
    email         = email,
    passwordMasked = passwordMasked,
    roomName      = roomName,
    ownInviteCode = ownInviteCode,
    hasPartner    = partner != null,
    partnerUsername = partner?.username,
    partnerEmail  = partner?.email,
    role          = role
)
