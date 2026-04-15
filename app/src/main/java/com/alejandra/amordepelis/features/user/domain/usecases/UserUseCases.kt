package com.alejandra.amordepelis.features.user.domain.usecases

data class UserUseCases(
    val getUserProfile: GetUserProfileUseCase,
    val updateUserProfile: UpdateUserProfileUseCase,
    val deleteUser: DeleteUserUseCase,
    val joinVirtualRoom: JoinVirtualRoomUseCase
)
