package com.alejandra.amordepelis.features.home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alejandra.amordepelis.features.auth.presentation.screens.AuthUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, error = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, error = null) }
    }

    fun onFirstNameChange(firstName: String) {
        _uiState.update { it.copy(firstName = firstName, error = null) }
    }

    fun onSecondNameChange(secondName: String) {
        _uiState.update { it.copy(secondName = secondName, error = null) }
    }

    fun login() {
        val state = _uiState.value
        if (!validateLoginInput(state.email, state.password)) return

        _uiState.update { it.copy(isLoading = true, error = null, isSuccess = false) }

        viewModelScope.launch {
            try {
                // TODO: Replace with actual login use case
                delay(1500) // Simulated network call

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = true,
                        message = "Inicio de sesión exitoso"
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = false,
                        error = e.message ?: "Error desconocido"
                    )
                }
            }
        }
    }

    fun register(email: String, password: String, confirmPassword: String) {
        if (!validateRegisterInput(email, password, confirmPassword)) return

        _uiState.update { it.copy(isLoading = true, error = null, isSuccess = false) }

        viewModelScope.launch {
            try {
                // TODO: Replace with actual register use case
                delay(1500) // Simulated network call

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = true,
                        message = "Registro exitoso"
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = false,
                        error = e.message ?: "Error desconocido"
                    )
                }
            }
        }
    }

    fun forgotPassword(email: String) {
        if (email.isBlank()) {
            _uiState.update { it.copy(error = "Por favor ingresa tu correo electrónico") }
            return
        }

        if (!isValidEmail(email)) {
            _uiState.update { it.copy(error = "Por favor ingresa un correo electrónico válido") }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null, isSuccess = false) }

        viewModelScope.launch {
            try {
                // TODO: Replace with actual forgot password use case
                delay(1500) // Simulated network call

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = true,
                        message = "Se ha enviado un correo para restablecer tu contraseña"
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = false,
                        error = e.message ?: "Error desconocido"
                    )
                }
            }
        }
    }

    fun registerCouple() {
        val state = _uiState.value
        if (state.firstName.isBlank()) {
            _uiState.update { it.copy(error = "Por favor ingresa el nombre de la primera persona") }
            return
        }

        if (state.secondName.isBlank()) {
            _uiState.update { it.copy(error = "Por favor ingresa el nombre de la segunda persona") }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null, isSuccess = false) }

        viewModelScope.launch {
            try {
                // TODO: Replace with actual couple registration use case
                delay(1500) // Simulated network call

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = true,
                        message = "¡Bienvenidos ${state.firstName} y ${state.secondName}!"
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = false,
                        error = e.message ?: "Error desconocido"
                    )
                }
            }
        }
    }

    fun clearState() {
        _uiState.value = AuthUiState()
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    private fun validateLoginInput(email: String, password: String): Boolean {
        if (email.isBlank()) {
            _uiState.update { it.copy(error = "Por favor ingresa tu correo electrónico") }
            return false
        }

        if (!isValidEmail(email)) {
            _uiState.update { it.copy(error = "Por favor ingresa un correo electrónico válido") }
            return false
        }

        if (password.isBlank()) {
            _uiState.update { it.copy(error = "Por favor ingresa tu contraseña") }
            return false
        }

        if (password.length < 6) {
            _uiState.update { it.copy(error = "La contraseña debe tener al menos 6 caracteres") }
            return false
        }

        return true
    }

    private fun validateRegisterInput(email: String, password: String, confirmPassword: String): Boolean {
        if (!validateLoginInput(email, password)) return false

        if (password != confirmPassword) {
            _uiState.update { it.copy(error = "Las contraseñas no coinciden") }
            return false
        }

        return true
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
