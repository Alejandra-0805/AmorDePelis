package com.alejandra.amordepelis.features.movies.domain.validators

/**
 * Resultado de una validación.
 * Sealed class que encapsula éxito o error.
 */
sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
}

/**
 * Interfaz para validación de formularios de película.
 * SOLID: Depender de abstracción, no implementación.
 * Facilita testing con mocks.
 */
interface MovieFormValidator {
    fun validateTitle(title: String): ValidationResult
    fun validateSynopsis(synopsis: String?): ValidationResult
    fun validateDuration(duration: Int?): ValidationResult
    fun validateTags(tags: Set<String>): ValidationResult
    fun validateAll(title: String, synopsis: String?, duration: Int?, tags: Set<String>): ValidationResult
}

/**
 * Implementación de MovieFormValidator.
 * Centraliza toda la lógica de validación de películas.
 * SOLID: SRP (única responsabilidad = validar)
 */
class MovieFormValidatorImpl : MovieFormValidator {
    companion object {
        private const val MIN_TITLE_LENGTH = 1
        private const val MAX_TITLE_LENGTH = 255
        private const val MAX_SYNOPSIS_LENGTH = 2000
        private const val MAX_TAGS = 10
        private const val MIN_DURATION = 1
        private const val MAX_DURATION = 500
    }

    override fun validateTitle(title: String): ValidationResult {
        return when {
            title.isBlank() -> ValidationResult.Error("El título es obligatorio")
            title.length < MIN_TITLE_LENGTH -> ValidationResult.Error("Título muy corto")
            title.length > MAX_TITLE_LENGTH -> ValidationResult.Error("Título muy largo (máx. 255)")
            else -> ValidationResult.Success
        }
    }

    override fun validateSynopsis(synopsis: String?): ValidationResult {
        return when {
            synopsis == null -> ValidationResult.Success
            synopsis.isBlank() -> ValidationResult.Success
            synopsis.length > MAX_SYNOPSIS_LENGTH -> ValidationResult.Error("Sinopsis muy larga (máx. 2000 caracteres)")
            else -> ValidationResult.Success
        }
    }

    override fun validateDuration(duration: Int?): ValidationResult {
        return when {
            duration == null -> ValidationResult.Success
            duration < MIN_DURATION -> ValidationResult.Error("Duración inválida")
            duration > MAX_DURATION -> ValidationResult.Error("Duración máxima: $MAX_DURATION minutos")
            else -> ValidationResult.Success
        }
    }

    override fun validateTags(tags: Set<String>): ValidationResult {
        return when {
            tags.isEmpty() -> ValidationResult.Success
            tags.size > MAX_TAGS -> ValidationResult.Error("Máximo $MAX_TAGS tags permitidos")
            tags.any { it.isBlank() || it.length > 50 } -> ValidationResult.Error("Tags inválidos (máx. 50 caracteres cada uno)")
            else -> ValidationResult.Success
        }
    }

    override fun validateAll(
        title: String,
        synopsis: String?,
        duration: Int?,
        tags: Set<String>
    ): ValidationResult {
        val titleResult = validateTitle(title)
        if (titleResult is ValidationResult.Error) return titleResult

        val synopsisResult = validateSynopsis(synopsis)
        if (synopsisResult is ValidationResult.Error) return synopsisResult

        val durationResult = validateDuration(duration)
        if (durationResult is ValidationResult.Error) return durationResult

        val tagsResult = validateTags(tags)
        if (tagsResult is ValidationResult.Error) return tagsResult

        return ValidationResult.Success
    }
}
