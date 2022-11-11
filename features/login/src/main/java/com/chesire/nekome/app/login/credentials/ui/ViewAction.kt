package com.chesire.nekome.app.login.credentials.ui

sealed class ViewAction {
    data class UsernameChanged(val newUsername: String) : ViewAction()
    data class PasswordChanged(val newPassword: String) : ViewAction()
    object ForgotPasswordPressed : ViewAction()
    object LoginPressed : ViewAction()
    object SignUpPressed : ViewAction()
}
