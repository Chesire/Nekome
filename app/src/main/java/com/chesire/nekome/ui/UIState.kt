package com.chesire.nekome.ui

data class UIState(
    val isInitialized: Boolean,
    val userLoggedIn: Boolean,
    val defaultHomeScreen: String,
    val kickUserToLogin: Unit?
) {
    companion object {
        val empty = UIState(
            isInitialized = false,
            userLoggedIn = false,
            defaultHomeScreen = Screen.Anime.route,
            kickUserToLogin = null
        )
    }
}
