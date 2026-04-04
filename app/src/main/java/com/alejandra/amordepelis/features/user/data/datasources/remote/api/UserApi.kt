package com.alejandra.amordepelis.features.user.data.datasources.remote.api

import com.alejandra.amordepelis.features.user.data.datasources.remote.model.PartnerInvitationRequestDto
import com.alejandra.amordepelis.features.user.data.datasources.remote.model.UserProfileDto
import com.alejandra.amordepelis.features.user.data.datasources.remote.model.UserSearchResultDto

interface UserApi {
    suspend fun getUserProfile(): UserProfileDto
    suspend fun searchUsersByUsername(username: String): List<UserSearchResultDto>
    suspend fun sendPartnerInvitation(request: PartnerInvitationRequestDto)
}
