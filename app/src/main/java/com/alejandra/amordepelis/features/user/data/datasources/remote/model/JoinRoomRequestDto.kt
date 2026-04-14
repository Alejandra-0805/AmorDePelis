package com.alejandra.amordepelis.features.user.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class JoinRoomRequestDto(
    @SerializedName("invitationCode")
    val invitationCode: String
)
