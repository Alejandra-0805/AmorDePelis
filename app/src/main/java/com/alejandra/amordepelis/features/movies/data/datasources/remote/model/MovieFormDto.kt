package com.alejandra.amordepelis.features.movies.data.datasources.remote.model

import okhttp3.RequestBody

/**
 * DTO que encapsula TODOS los datos que se envían al backend.
 * Responsabilidad única: Serialización/deserialización de datos.
 * SOLID: SRP - No tiene lógica de negocio, solo datos.
 */
data class MovieFormDto(
    val title: String,
    val synopsis: String?,
    val durationMinutes: Int?,
    val tags: String?, // CSV format: "tag1,tag2,tag3"
    val imageBody: RequestBody? = null // Multipart file
)
