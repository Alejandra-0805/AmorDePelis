package com.alejandra.amordepelis.features.movies.domain.usecases

import com.alejandra.amordepelis.features.movies.domain.entities.MovieFormData
import com.alejandra.amordepelis.features.movies.domain.validators.MovieFormValidator
import com.alejandra.amordepelis.features.movies.domain.validators.ValidationResult
import javax.inject.Inject

/**
 * Use Case: Valida datos de formulario sin efectos secundarios.
 * Responsabilidad única: Ejecutar validación de película.
 * SOLID: SRP, OCP, DIP
 */
class ValidateMovieFormUseCase @Inject constructor(
    private val validator: MovieFormValidator
) {
    operator fun invoke(formData: MovieFormData): ValidationResult {
        return validator.validateAll(
            title = formData.title,
            synopsis = formData.synopsis,
            duration = formData.durationMinutes,
            tags = formData.tags
        )
    }
}
