package com.alejandra.amordepelis.features.movies.domain.usecases

import com.alejandra.amordepelis.features.movies.domain.entities.Movie
import com.alejandra.amordepelis.features.movies.domain.repositories.MoviesRepository
import java.io.File
import javax.inject.Inject

class AddMovieUseCase @Inject constructor(
    private val repository: MoviesRepository
) {
    suspend operator fun invoke(
        title: String,
        synopsis: String?,
        durationMinutes: Int?,
        tags: String?,
        imageFile: File?
    ): Movie {
        if (title.isBlank()) {
            throw IllegalArgumentException("El título es requerido")
        }
        return repository.addMovie(title, synopsis, durationMinutes, tags, imageFile)
    }
}
