package com.alejandra.amordepelis.features.auth.data.datasources.remote.mapper

import com.alejandra.amordepelis.features.auth.data.datasources.remote.model.RegisterResponseDto
import com.alejandra.amordepelis.features.auth.domain.entities.RegisterResponse

fun RegisterResponseDto.registerToDomain(): RegisterResponse {
    return RegisterResponse(
        id = this.id,
        email = this.email,
        message = this.message
    )
}