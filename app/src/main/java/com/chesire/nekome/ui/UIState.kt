package com.chesire.nekome.ui

data class UIState(
    val userLoggedIn: Boolean,
    val defaultHomeScreen: String
) {
    companion object {
        val empty = UIState(
            userLoggedIn = false,
            defaultHomeScreen = Nav.Series.Anime.route
        )
    }
}
