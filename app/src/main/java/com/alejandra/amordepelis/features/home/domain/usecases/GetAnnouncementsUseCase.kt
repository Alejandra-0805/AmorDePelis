package com.alejandra.amordepelis.features.home.domain.usecases

import com.alejandra.amordepelis.features.home.domain.entities.Announcement
import com.alejandra.amordepelis.features.home.domain.repositories.HomeRepository
import javax.inject.Inject

class GetAnnouncementsUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): List<Announcement> {
        return repository.getAnnouncements()
    }
}
