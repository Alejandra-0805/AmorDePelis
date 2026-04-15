package com.alejandra.amordepelis.core.navigation

import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object Register

@Serializable
object Home

@Serializable
object Lists

@Serializable
data class ListDetails(val listId: String)

@Serializable
object AddList

@Serializable
object Movies

@Serializable
data class MovieDetails(val movieId: String)

@Serializable
object AddMovie

@Serializable
object User

@Serializable
object AddAnnouncement