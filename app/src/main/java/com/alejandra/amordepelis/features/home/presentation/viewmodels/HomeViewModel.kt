package com.alejandra.amordepelis.features.home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alejandra.amordepelis.core.network.connectivity.ConnectivityManager
import com.alejandra.amordepelis.core.storage.SessionManager
import com.alejandra.amordepelis.core.storage.UserRole
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
    private val homeUseCases: HomeUseCases,
    private val sessionManager: SessionManager,
    private val connectivityManager: ConnectivityManager
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
        viewModelScope.launch {
            connectivityManager.isConnected.collect { isConnected ->
                _uiState.update { it.copy(isConnected = isConnected) }
                if (isConnected && _uiState.value.hasOfflineData) {
                    _message.value = "Conexión restaurada - sincronizando datos..."
                    loadHome()
                }
            }
        }

        viewModelScope.launch {
            sessionManager.currentRole.collect { role ->
                _uiState.update { it.copy(showAddFirstMovieButton = role != UserRole.PAREJA) }
            }
        }

        loadHome()
    }

    fun loadHome() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            runCatching {
                val movies = homeUseCases.getAllMovies()
                val allNews = homeUseCases.getAllNews()
                val latestNews = try { homeUseCases.getLatestNews() } catch (_: Exception) { null }

                val hasData = movies.isNotEmpty()
                val isOffline = !connectivityManager.isNetworkAvailable()
                
                _uiState.update {
                    it.copy(
                        moviesWatched = movies.size,
                        recentMovies = movies.take(5).map { movie ->
                            RecentMovieUiModel(
                                id = movie.id,
                                title = movie.title,
                                imageUrl = movie.imageUrl
                            )
                        },
                        latestNews = latestNews?.let { news ->
                            AnnouncementUiModel(
                                id = news.id,
                                title = news.title,
                                description = news.description,
                                imageUrl = news.imageUrl
                            )
                        },
                        isLoading = false,
                        hasOfflineData = hasData && isOffline
                    )
                }

                _announcements.value = allNews.map { announcement ->
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

