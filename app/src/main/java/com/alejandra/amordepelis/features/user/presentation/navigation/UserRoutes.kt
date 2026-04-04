package com.alejandra.amordepelis.features.user.presentation.navigation

sealed class UserRoutes(val route: String) {
    data object UserProfile : UserRoutes("user/profile")
    data object PartnerSearch : UserRoutes("user/partner-search")
}
