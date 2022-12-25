package com.chesire.nekome.app.login.credentials.ui

import androidx.annotation.StringRes

data class UIState(
    val username: String,
    val hasUsernameError: Boolean,
    val password: String,
    val hasPasswordError: Boolean,
    val isPerformingLogin: Boolean,
    val loginButtonEnabled: Boolean,
    @StringRes val errorSnackbarMessage: Int?,
    val navigateScreenEvent: Boolean?
) {
    companion object {
        val empty: UIState
            get() = UIState(
                username = "",
                hasUsernameError = false,
                password = "",
                hasPasswordError = false,
                isPerformingLogin = false,
                loginButtonEnabled = false,
                errorSnackbarMessage = null,
                navigateScreenEvent = null
            )
    }
}
