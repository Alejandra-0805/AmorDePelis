package com.alejandra.amordepelis.features.lists.domain.usecases

import com.alejandra.amordepelis.features.lists.domain.repositories.ListsRepository
import javax.inject.Inject

class AddMovieToListUseCase @Inject constructor(
    private val repository: ListsRepository
) {
    suspend operator fun invoke(roomId: Int, listId: Int, movieId: Int) {
        repository.addMovieToList(roomId, listId, movieId)
    }
}
