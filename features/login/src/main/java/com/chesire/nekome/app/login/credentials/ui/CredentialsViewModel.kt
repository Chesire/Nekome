package com.chesire.nekome.app.login.credentials.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekome.app.login.R
import com.chesire.nekome.app.login.credentials.core.ClearCredentialsUseCase
import com.chesire.nekome.app.login.credentials.core.PopulateUserDetailsUseCase
import com.chesire.nekome.app.login.credentials.core.VerifyCredentialsFailure
import com.chesire.nekome.app.login.credentials.core.VerifyCredentialsUseCase
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CredentialsViewModel @Inject constructor(
    private val verifyCredentials: VerifyCredentialsUseCase,
    private val populateUserDetails: PopulateUserDetailsUseCase,
    private val clearCredentials: ClearCredentialsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState.empty)
    val uiState = _uiState.asStateFlow()
    private var state: UIState
        get() = _uiState.value
        set(value) {
            _uiState.update { value }
        }

    fun execute(viewAction: ViewAction) {
        when (viewAction) {
            is ViewAction.UsernameChanged -> state = state.copy(
                username = viewAction.newUsername,
                usernameError = false,
                buttonEnabled = viewAction.newUsername.isNotBlank() && state.password.isNotBlank()
            )
            is ViewAction.PasswordChanged -> state = state.copy(
                password = viewAction.newPassword,
                passwordError = false,
                buttonEnabled = state.username.isNotBlank() && viewAction.newPassword.isNotBlank()
            )
            ViewAction.LoginPressed -> performLogin()
            ViewAction.ErrorSnackbarObserved -> {
                state = state.copy(errorSnackbar = ErrorSnackbar(false, 0))
            }
        }
    }

    private fun performLogin() = viewModelScope.launch {
        state = state.copy(isPerformingLogin = true)

        verifyCredentials(username = state.username, password = state.password)
            .onSuccess { loadUserDetails() }
            .onFailure(::handleLoginFailure)
    }

    private suspend fun loadUserDetails() {
        populateUserDetails()
            .onSuccess {
                state = state.copy(
                    isPerformingLogin = false
                )
            }
            .onFailure {
                clearCredentials()
                handleLoginFailure(VerifyCredentialsFailure.NetworkError)
            }
    }

    private fun handleLoginFailure(failure: VerifyCredentialsFailure) {
        val newState = when (failure) {
            VerifyCredentialsFailure.InvalidCredentials -> state.copy(
                isPerformingLogin = false,
                errorSnackbar = ErrorSnackbar(true, R.string.login_error_credentials)
            )
            VerifyCredentialsFailure.NetworkError -> state.copy(
                isPerformingLogin = false,
                errorSnackbar = ErrorSnackbar(true, R.string.login_error_generic)
            )
            VerifyCredentialsFailure.PasswordInvalid -> state.copy(
                passwordError = true,
                isPerformingLogin = false
            )
            VerifyCredentialsFailure.UsernameInvalid -> state.copy(
                usernameError = true,
                isPerformingLogin = false
            )
        }
        state = newState
    }
}
