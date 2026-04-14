package com.alejandra.amordepelis.features.auth.domain.usecases

data class AuthUseCases(
    val login: LoginUseCase,
    val register: RegisterUseCase,
    val saveToken: SaveTokenUseCase,
    val createRoom: CreateAutoRoomUseCase
)
