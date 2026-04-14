package com.alejandra.amordepelis.features.user.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class RoomResponseDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("roomName")
    val roomName: String,
    @SerializedName("invitationCode")
    val invitationCode: String,
    @SerializedName("creatorId")
    val creatorId: Int,
    @SerializedName("guestId")
    val guestId: Int?
)
