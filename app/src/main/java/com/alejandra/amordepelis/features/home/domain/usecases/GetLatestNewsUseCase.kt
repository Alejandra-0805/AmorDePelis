package com.alejandra.amordepelis.features.home.domain.usecases

import com.alejandra.amordepelis.features.home.domain.entities.Announcement
import com.alejandra.amordepelis.features.home.domain.repositories.HomeRepository
import javax.inject.Inject

class GetLatestNewsUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): Announcement {
        return repository.getLatestNews()
    }
}
