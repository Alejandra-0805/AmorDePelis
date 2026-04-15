package com.alejandra.amordepelis.features.home.domain.usecases

import com.alejandra.amordepelis.features.home.domain.entities.Announcement
import com.alejandra.amordepelis.features.home.domain.repositories.HomeRepository
import java.io.File
import javax.inject.Inject

class CreateAnnouncementUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(title: String, content: String, imageFile: File?): Announcement {
        return repository.createAnnouncement(title, content, imageFile)
    }
}
