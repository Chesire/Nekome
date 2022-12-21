package com.chesire.nekome.app.login.credentials.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _viewState = MutableStateFlow(ViewState.empty)
    val viewState: StateFlow<ViewState> = _viewState

    fun execute(viewAction: ViewAction) {
        when (viewAction) {
            is ViewAction.UsernameChanged -> _viewState.update { viewState ->
                viewState.copy(
                    username = viewAction.newUsername,
                    usernameError = false,
                    buttonEnabled = viewAction.newUsername.isNotBlank() && viewState.password.isNotBlank()
                )
            }
            is ViewAction.PasswordChanged -> _viewState.update { viewState ->
                viewState.copy(
                    password = viewAction.newPassword,
                    passwordError = false,
                    buttonEnabled = viewState.username.isNotBlank() && viewAction.newPassword.isNotBlank()
                )
            }
            ViewAction.ForgotPasswordPressed -> TODO()
            ViewAction.LoginPressed -> performLogin()
            ViewAction.SignUpPressed -> TODO()
        }
    }

    private fun performLogin() = viewModelScope.launch {
        _viewState.update { viewState -> viewState.copy(isPerformingLogin = true) }

        val state = viewState.value
        verifyCredentials(username = state.username, password = state.password)
            .onSuccess {
                // TODO: Get user details
            }
            .onFailure {
                when (it) {
                    VerifyCredentialsFailure.InvalidCredentials -> _viewState.update { viewState ->
                        // TODO: Show error message
                        viewState.copy(
                            isPerformingLogin = false
                        )
                    }
                    VerifyCredentialsFailure.NetworkError -> _viewState.update { viewState ->
                        // TODO: Show error message
                        viewState.copy(
                            isPerformingLogin = false
                        )
                    }
                    VerifyCredentialsFailure.PasswordInvalid -> _viewState.update { viewState ->
                        viewState.copy(
                            usernameError = true,
                            isPerformingLogin = false
                        )
                    }
                    VerifyCredentialsFailure.UsernameInvalid -> _viewState.update { viewState ->
                        viewState.copy(
                            passwordError = true,
                            isPerformingLogin = false
                        )
                    }
                }
            }
    }
}
