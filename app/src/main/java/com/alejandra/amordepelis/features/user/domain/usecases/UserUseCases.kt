package com.alejandra.amordepelis.features.user.domain.usecases

data class UserUseCases(
    val getUserProfile: GetUserProfileUseCase,
    val searchUsersByUsername: SearchUsersByUsernameUseCase,
    val sendPartnerInvitation: SendPartnerInvitationUseCase,
    val updateUserProfile: UpdateUserProfileUseCase,
    val deleteUser: DeleteUserUseCase,
    val joinVirtualRoom: JoinVirtualRoomUseCase
)
