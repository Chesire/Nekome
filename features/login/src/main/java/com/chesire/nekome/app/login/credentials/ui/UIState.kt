package com.chesire.nekome.app.login.credentials.ui

import androidx.annotation.StringRes

data class UIState(
    val username: String,
    val usernameError: Boolean,
    val password: String,
    val passwordError: Boolean,
    val isPerformingLogin: Boolean,
    val buttonEnabled: Boolean,
    @StringRes val errorSnackbarMessage: Int?
) {
    companion object {
        val empty: UIState
            get() = UIState(
                username = "",
                usernameError = false,
                password = "",
                passwordError = false,
                isPerformingLogin = false,
                buttonEnabled = false,
                errorSnackbarMessage = null
            )
    }
}
