package com.alejandra.amordepelis.features.user.data.datasources.remote.mapper

import com.alejandra.amordepelis.features.user.data.datasources.remote.model.PartnerInvitationRequestDto

fun String.toPartnerInvitationRequestDto(): PartnerInvitationRequestDto {
    return PartnerInvitationRequestDto(targetUserId = this)
}
