package com.chesire.nekome.app.settings.config.ui

data class UIState(
    val title: String
) {
    companion object {
        val default = UIState(
            title = ""
        )
    }
}
