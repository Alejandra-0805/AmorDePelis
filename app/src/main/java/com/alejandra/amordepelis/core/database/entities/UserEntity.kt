package com.alejandra.amordepelis.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int,
    val username: String,
    val email: String,
    val passwordMasked: String,
    val roomName: String? = null,
    val ownInviteCode: String? = null
)
