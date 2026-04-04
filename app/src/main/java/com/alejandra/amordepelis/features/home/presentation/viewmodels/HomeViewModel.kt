package com.alejandra.amordepelis.features.home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alejandra.amordepelis.features.home.domain.usecases.GetMetricsUseCase
import com.alejandra.amordepelis.features.home.domain.usecases.GetRecentMoviesUseCase
import com.alejandra.amordepelis.features.home.domain.usecases.HomeUseCases
import com.alejandra.amordepelis.features.home.presentation.screens.HomeBottomTab
import com.alejandra.amordepelis.features.home.presentation.screens.HomeUiState
import com.alejandra.amordepelis.features.home.presentation.screens.RecentMovieUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val homeUseCases: HomeUseCases = HomeUseCases(
        login = GetMetricsUseCase(),
        register = GetRecentMoviesUseCase()
    )
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadHome()
    }

    fun loadHome() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            runCatching {
                val metrics = fetchMetricsFromUseCase()
                val recentMovies = fetchRecentMoviesFromUseCase()

                _uiState.update {
                    it.copy(
                        firstPersonName = metrics.firstPersonName,
                        secondPersonName = metrics.secondPersonName,
                        moviesWatched = metrics.moviesWatched,
                        favorites = metrics.favorites,
                        averageRating = metrics.averageRating,
                        lists = metrics.lists,
                        recentMovies = recentMovies,
                        isLoading = false
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = throwable.message ?: "No se pudo cargar el inicio"
                    )
                }
            }
        }
    }

    fun onBottomTabSelected(tab: HomeBottomTab) {
        _uiState.update { it.copy(selectedBottomTab = tab) }
    }

    fun onAddFirstMovieClick() {
        _uiState.update { it.copy(message = "Próximamente podrás registrar tu primera película") }
    }

    fun clearMessage() {
        _uiState.update { it.copy(message = null) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    private fun fetchMetricsFromUseCase(): HomeMetricsResult {
        // Home module use cases are connected here; replace these defaults when contracts are implemented.
        homeUseCases.login
        return HomeMetricsResult(
            firstPersonName = "Persona 1",
            secondPersonName = "Persona 2",
            moviesWatched = 0,
            favorites = 0,
            averageRating = 0.0,
            lists = 2
        )
    }

    private fun fetchRecentMoviesFromUseCase(): List<RecentMovieUiModel> {
        homeUseCases.register
        return listOf(
            RecentMovieUiModel(
                id = "1",
                title = "Titanic",
                rating = 4,
                durationMinutes = 127,
                genre = "Romance"
            ),
            RecentMovieUiModel(
                id = "2",
                title = "Yo antes de ti",
                rating = 4,
                durationMinutes = 127,
                genre = "Romance"
            ),
            RecentMovieUiModel(
                id = "3",
                title = "Harry Potter",
                rating = 4,
                durationMinutes = 127,
                genre = "Fantasía"
            )
        )
    }
}

private data class HomeMetricsResult(
    val firstPersonName: String,
    val secondPersonName: String,
    val moviesWatched: Int,
    val favorites: Int,
    val averageRating: Double,
    val lists: Int
)
