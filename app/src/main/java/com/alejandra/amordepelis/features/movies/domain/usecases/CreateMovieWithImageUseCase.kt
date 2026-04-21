package com.alejandra.amordepelis.features.movies.domain.usecases

import com.alejandra.amordepelis.features.movies.data.datasources.remote.mapper.MovieFormDtoFactory
import com.alejandra.amordepelis.features.movies.domain.entities.Movie
import com.alejandra.amordepelis.features.movies.domain.entities.MovieFormData
import com.alejandra.amordepelis.features.movies.domain.repositories.MoviesRepository
import com.alejandra.amordepelis.features.movies.domain.validators.MovieFormValidator
import com.alejandra.amordepelis.features.movies.domain.validators.ValidationResult
import java.io.File
import javax.inject.Inject

/**
 * Use Case: Crea película con imagen.
 * Responsabilidades:
 * - Validar datos
 * - Convertir File a formato requerido
 * - Llamar al repositorio
 * SOLID: SRP (una responsabilidad = crear película), DIP (depende de repository interface)
 */
class CreateMovieWithImageUseCase @Inject constructor(
    private val repository: MoviesRepository,
    private val validator: MovieFormValidator,
    private val dtoFactory: MovieFormDtoFactory
) {
    suspend operator fun invoke(
        formData: MovieFormData,
        imageFile: File? = null
    ): Result<Movie> = runCatching {
        // 1. Validar
        val validationResult = validator.validateAll(
            title = formData.title,
            synopsis = formData.synopsis,
            duration = formData.durationMinutes,
            tags = formData.tags
        )

        if (validationResult is ValidationResult.Error) {
            throw IllegalArgumentException(validationResult.message)
        }

        // 2. Convertir a DTO
        val dto = dtoFactory.createFromDomainData(formData, imageFile)

        // 3. Enviar al repositorio
        repository.addMovieWithImage(
            title = dto.title,
            synopsis = dto.synopsis,
            durationMinutes = dto.durationMinutes,
            tags = dto.tags,
            imageFile = imageFile
        )
    }
}
