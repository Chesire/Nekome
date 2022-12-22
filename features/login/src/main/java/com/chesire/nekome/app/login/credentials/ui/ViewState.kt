package com.chesire.nekome.app.login.credentials.ui

import androidx.annotation.StringRes

data class ViewState(
    val username: String,
    val usernameError: Boolean,
    val password: String,
    val passwordError: Boolean,
    val isPerformingLogin: Boolean,
    val buttonEnabled: Boolean,
    val errorSnackbar: ErrorSnackbar
) {
    companion object {
        val empty: ViewState
            get() = ViewState(
                username = "",
                usernameError = false,
                password = "",
                passwordError = false,
                isPerformingLogin = false,
                buttonEnabled = false,
                errorSnackbar = ErrorSnackbar(false, 0)
            )
    }
}

data class ErrorSnackbar(
    val show: Boolean,
    @StringRes val value: Int
)
