package com.alejandra.amordepelis.features.lists.domain.usecases

import com.alejandra.amordepelis.features.lists.domain.entities.Announcement
import com.alejandra.amordepelis.features.lists.domain.repositories.ListsRepository
import javax.inject.Inject

class GetAnnouncementsUseCase @Inject constructor(
    private val repository: ListsRepository
) {
    suspend operator fun invoke(): List<Announcement> = repository.getAnnouncements()
}
