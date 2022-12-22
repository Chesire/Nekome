package com.chesire.nekome.app.login.credentials.ui

sealed interface ViewAction {
    data class UsernameChanged(val newUsername: String) : ViewAction
    data class PasswordChanged(val newPassword: String) : ViewAction
    object LoginPressed : ViewAction
    object ErrorSnackbarObserved : ViewAction
}
