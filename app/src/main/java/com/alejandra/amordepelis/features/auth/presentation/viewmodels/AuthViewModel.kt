package com.alejandra.amordepelis.features.auth.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alejandra.amordepelis.features.auth.data.datasources.remote.model.LoginRequest
import com.alejandra.amordepelis.features.auth.data.datasources.remote.model.RegisterRequest
import com.alejandra.amordepelis.features.auth.domain.usecases.AuthUseCases
import com.alejandra.amordepelis.features.auth.presentation.screens.AuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _username = MutableStateFlow("")
    val username = _username.asStateFlow()

    private val _role = MutableStateFlow("PAREJA")
    val role = _role.asStateFlow()

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun onUsernameChange(newUsername: String) {
        _username.value = newUsername
    }

    fun onRoleChange(newRole: String) {
        _role.value = newRole
    }

    fun login(request: LoginRequest) {
        _uiState.update {
            it.copy(
                isLoading = true,
                error = null,
                isSuccess = false
            )
        }

        viewModelScope.launch {
            val result = authUseCases.login(request)
            Log.d("AuthViewModel", "Login result: $result")

            result.fold(
                onSuccess = { response ->
                    authUseCases.saveToken(response.token)
                    Log.d("AuthViewModel", "Token saved successfully")

                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            isSuccess = true,
                            message = response.role
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                }
            )
        }
    }

    fun register(request: RegisterRequest) {
        _uiState.update {
            it.copy(
                isLoading = true,
                error = null,
                isSuccess = false
            )
        }

        viewModelScope.launch {
            val result = authUseCases.register(request)
            Log.d("AuthViewModel", "Register result: $result")

            result.fold(
                onSuccess = { response ->
                    Log.d("AuthViewModel", "Register successful, auto-login...")
                    performAutoLogin(
                        email = request.email,
                        password = request.passwordRaw
                    )
                },
                onFailure = { error ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                }
            )
        }
    }

    private suspend fun performAutoLogin(email: String, password: String) {
        val loginResult = authUseCases.login(LoginRequest(email = email, passwordRaw = password))
        Log.d("AuthViewModel", "Auto-login result: $loginResult")

        loginResult.fold(
            onSuccess = { response ->
                authUseCases.saveToken(response.token)
                Log.d("AuthViewModel", "Token saved after auto-login")

                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        isSuccess = true,
                        message = "Registro e inicio de sesión exitosos"
                    )
                }
            },
            onFailure = { error ->
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        error = "Registro exitoso pero falló el inicio de sesión: ${error.message}"
                    )
                }
            }
        )
    }

    fun clearState() {
        _uiState.value = AuthUiState()
        _email.value = ""
        _password.value = ""
        _username.value = ""
        _role.value = "PAREJA"
    }
}
