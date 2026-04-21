package com.alejandra.amordepelis.features.movies.data.datasources.remote.mapper

import com.alejandra.amordepelis.features.movies.data.datasources.remote.model.MovieFormDto
import com.alejandra.amordepelis.features.movies.domain.entities.MovieFormData
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

/**
 * Factory para crear DTOs desde domain entities.
 * Responsabilidad única: Transformación entre capas.
 * SOLID: SRP (transforma), DIP (interfaz)
 */
interface MovieFormDtoFactory {
    fun createFromDomainData(formData: MovieFormData, imageFile: File?): MovieFormDto
}

/**
 * Implementación del factory.
 */
class MovieFormDtoFactoryImpl @Inject constructor() : MovieFormDtoFactory {
    override fun createFromDomainData(formData: MovieFormData, imageFile: File?): MovieFormDto {
        return MovieFormDto(
            title = formData.title,
            synopsis = formData.synopsis,
            durationMinutes = formData.durationMinutes,
            tags = formData.tags.joinToString(",").takeIf { it.isNotEmpty() },
            imageBody = imageFile?.asRequestBody("image/*".toMediaTypeOrNull())
        )
    }
}
