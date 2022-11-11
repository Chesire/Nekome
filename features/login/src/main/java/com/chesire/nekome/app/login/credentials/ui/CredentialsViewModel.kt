package com.chesire.nekome.app.login.credentials.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class CredentialsViewModel @Inject constructor() : ViewModel() {

    private val _viewState = MutableStateFlow(ViewState.empty)
    val viewState: StateFlow<ViewState> = _viewState

    fun execute(viewAction: ViewAction) {
        when (viewAction) {
            is ViewAction.UsernameChanged -> _viewState.update { viewState ->
                viewState.copy(
                    username = viewAction.newUsername
                )
            }
            is ViewAction.PasswordChanged -> _viewState.update { viewState ->
                viewState.copy(
                    password = viewAction.newPassword
                )
            }
            ViewAction.ForgotPasswordPressed -> TODO()
            ViewAction.LoginPressed -> performLogin()
            ViewAction.SignUpPressed -> TODO()
        }
    }

    private fun performLogin() {

    }
}
