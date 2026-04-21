package com.alejandra.amordepelis.features.movies.domain.entities

/**
 * Value Object que representa datos validados de un formulario de película.
 * SOLID: Encapsula validación y es inmutable.
 * Es responsable de mantener invariantes de negocio.
 */
data class MovieFormData(
    val title: String,
    val synopsis: String? = null,
    val durationMinutes: Int? = null,
    val tags: Set<String> = emptySet(),
    val posterImageUri: String? = null
) {
    init {
        require(title.isNotBlank()) { "El título no puede estar vacío" }
        require(title.length <= 255) { "El título debe tener máximo 255 caracteres" }
        require((synopsis?.length ?: 0) <= 2000) { "La sinopsis debe tener máximo 2000 caracteres" }
        require(durationMinutes == null || durationMinutes > 0) { "La duración debe ser mayor a 0" }
        require(tags.all { it.isNotBlank() && it.length <= 50 }) { "Tags inválidos" }
    }

    companion object {
        fun builder() = MovieFormDataBuilder()
    }
}

/**
 * Builder para MovieFormData siguiendo el patrón Builder.
 * Facilita la construcción progresiva de objetos complejos.
 */
class MovieFormDataBuilder {
    private var title: String = ""
    private var synopsis: String? = null
    private var durationMinutes: Int? = null
    private var tags: Set<String> = emptySet()
    private var posterImageUri: String? = null

    fun title(value: String) = apply { this.title = value }
    fun synopsis(value: String?) = apply { this.synopsis = value }
    fun durationMinutes(value: Int?) = apply { this.durationMinutes = value }
    fun tags(value: Set<String>) = apply { this.tags = value }
    fun posterImageUri(value: String?) = apply { this.posterImageUri = value }

    fun build(): MovieFormData {
        return MovieFormData(
            title = title,
            synopsis = synopsis,
            durationMinutes = durationMinutes,
            tags = tags,
            posterImageUri = posterImageUri
        )
    }
}
