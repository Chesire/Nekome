package com.chesire.nekome.app.settings.config.ui

data class UIState(
    val themeValue: String
) {
    companion object {
        val default = UIState(
            themeValue = ""
        )
    }
}
