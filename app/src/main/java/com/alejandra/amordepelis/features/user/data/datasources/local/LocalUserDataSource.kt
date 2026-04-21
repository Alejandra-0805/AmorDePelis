package com.alejandra.amordepelis.features.user.data.datasources.local

import com.alejandra.amordepelis.core.database.dao.UserDao
import com.alejandra.amordepelis.features.user.data.datasources.local.mapper.toDomain
import com.alejandra.amordepelis.features.user.data.datasources.local.mapper.toEntity
import com.alejandra.amordepelis.features.user.domain.entities.UserProfile
import javax.inject.Inject

class LocalUserDataSource @Inject constructor(
    private val userDao: UserDao
) {
    suspend fun getProfileById(id: Int): UserProfile? =
        userDao.getById(id)?.toDomain()

    suspend fun getFirstCachedProfile(): UserProfile? =
        userDao.getFirst()?.toDomain()

    suspend fun saveProfile(profile: UserProfile) {
        userDao.upsert(profile.toEntity())
    }

    suspend fun clearProfile() {
        userDao.clearAll()
    }
}
