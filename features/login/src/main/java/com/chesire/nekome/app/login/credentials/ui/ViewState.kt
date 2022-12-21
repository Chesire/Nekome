package com.chesire.nekome.app.login.credentials.ui

data class ViewState(
    val username: String,
    val usernameError: Boolean,
    val password: String,
    val passwordError: Boolean,
    val isPerformingLogin: Boolean,
    val buttonEnabled: Boolean
) {
    companion object {
        val empty: ViewState
            get() = ViewState(
                username = "",
                usernameError = false,
                password = "",
                passwordError = false,
                isPerformingLogin = false,
                buttonEnabled = false
            )
    }
}
