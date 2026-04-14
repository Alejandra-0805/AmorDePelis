package com.alejandra.amordepelis.features.home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alejandra.amordepelis.features.home.domain.usecases.HomeUseCases
import com.alejandra.amordepelis.features.home.presentation.screens.AnnouncementUiModel
import com.alejandra.amordepelis.features.home.presentation.screens.HomeUiState
import com.alejandra.amordepelis.features.home.presentation.screens.RecentMovieUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCases: HomeUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    private val _announcements = MutableStateFlow<List<AnnouncementUiModel>>(emptyList())
    val announcements: StateFlow<List<AnnouncementUiModel>> = _announcements.asStateFlow()

    private val _currentAnnouncementIndex = MutableStateFlow(0)
    val currentAnnouncementIndex: StateFlow<Int> = _currentAnnouncementIndex.asStateFlow()

    init {
        loadHome()
    }

    fun loadHome() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            runCatching {
                val metrics = homeUseCases.getMetrics()
                val recentMovies = homeUseCases.getRecentMovies()
                val announcements = homeUseCases.getAnnouncements()

                _uiState.update {
                    it.copy(
                        firstPersonName = metrics.firstPersonName,
                        secondPersonName = metrics.secondPersonName,
                        moviesWatched = metrics.moviesWatched,
                        favorites = metrics.favorites,
                        averageRating = metrics.averageRating,
                        lists = metrics.lists,
                        recentMovies = recentMovies.map { movie ->
                            RecentMovieUiModel(
                                id = movie.id,
                                title = movie.title,
                                rating = movie.rating,
                                durationMinutes = movie.durationMinutes,
                                genre = movie.genre
                            )
                        },
                        isLoading = false
                    )
                }

                _announcements.value = announcements.map { announcement ->
                    AnnouncementUiModel(
                        id = announcement.id,
                        title = announcement.title,
                        description = announcement.description,
                        imageUrl = announcement.imageUrl
                    )
                }

                _isLoading.value = false
            }.onFailure { throwable ->
                _isLoading.value = false
                _error.value = throwable.message ?: "No se pudo cargar el inicio"
                _uiState.update { it.copy(isLoading = false, error = _error.value) }
            }
        }
    }

    fun onAddFirstMovieClick() {
        _message.value = "Próximamente podrás registrar tu primera película"
        _uiState.update { it.copy(message = _message.value) }
    }

    fun onAnnouncementIndexChanged(index: Int) {
        _currentAnnouncementIndex.value = index
    }

    fun clearMessage() {
        _message.value = null
        _uiState.update { it.copy(message = null) }
    }

    fun clearError() {
        _error.value = null
        _uiState.update { it.copy(error = null) }
    }
}
