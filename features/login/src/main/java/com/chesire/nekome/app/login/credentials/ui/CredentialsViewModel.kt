package com.chesire.nekome.app.login.credentials.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekome.app.login.R
import com.chesire.nekome.app.login.credentials.core.VerifyCredentialsFailure
import com.chesire.nekome.app.login.credentials.core.VerifyCredentialsUseCase
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CredentialsViewModel @Inject constructor(
    private val verifyCredentials: VerifyCredentialsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState.empty)
    val uiState: StateFlow<UIState> = _uiState

    fun execute(viewAction: ViewAction) {
        when (viewAction) {
            is ViewAction.UsernameChanged -> _uiState.update { viewState ->
                viewState.copy(
                    username = viewAction.newUsername,
                    usernameError = false,
                    buttonEnabled = viewAction.newUsername.isNotBlank() && viewState.password.isNotBlank()
                )
            }
            is ViewAction.PasswordChanged -> _uiState.update { viewState ->
                viewState.copy(
                    password = viewAction.newPassword,
                    passwordError = false,
                    buttonEnabled = viewState.username.isNotBlank() && viewAction.newPassword.isNotBlank()
                )
            }
            ViewAction.LoginPressed -> performLogin()
            ViewAction.ErrorSnackbarObserved -> _uiState.update { viewState ->
                viewState.copy(errorSnackbar = ErrorSnackbar(false, 0))
            }
        }
    }

    private fun performLogin() = viewModelScope.launch {
        _uiState.update { viewState -> viewState.copy(isPerformingLogin = true) }

        val state = uiState.value
        verifyCredentials(username = state.username, password = state.password)
            .onSuccess {
                // TODO: Finish this screen, make new screen for user details and series?
            }
            .onFailure(::handleCredentialsFailure)
    }

    private fun handleCredentialsFailure(failure: VerifyCredentialsFailure) {
        when (failure) {
            VerifyCredentialsFailure.InvalidCredentials -> _uiState.update { viewState ->
                viewState.copy(
                    isPerformingLogin = false,
                    errorSnackbar = ErrorSnackbar(true, R.string.login_error_credentials)
                )
            }
            VerifyCredentialsFailure.NetworkError -> _uiState.update { viewState ->
                viewState.copy(
                    isPerformingLogin = false,
                    errorSnackbar = ErrorSnackbar(true, R.string.login_error_generic)
                )
            }
            VerifyCredentialsFailure.PasswordInvalid -> _uiState.update { viewState ->
                viewState.copy(
                    passwordError = true,
                    isPerformingLogin = false
                )
            }
            VerifyCredentialsFailure.UsernameInvalid -> _uiState.update { viewState ->
                viewState.copy(
                    usernameError = true,
                    isPerformingLogin = false
                )
            }
        }
    }
}
