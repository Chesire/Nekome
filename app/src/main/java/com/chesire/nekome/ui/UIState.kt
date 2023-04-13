package com.chesire.nekome.ui

data class UIState(
    val userLoggedIn: Boolean,
    val defaultHomeScreen: String,
    val kickUserToLogin: Unit?
) {
    companion object {
        val empty = UIState(
            userLoggedIn = false,
            defaultHomeScreen = Screen.Anime.route,
            kickUserToLogin = null
        )
    }
}
