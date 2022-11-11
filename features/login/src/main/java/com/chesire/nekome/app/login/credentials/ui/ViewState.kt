package com.chesire.nekome.app.login.credentials.ui

data class ViewState(
    val username: String,
    val password: String
) {
    companion object {
        val empty: ViewState
            get() = ViewState("", "")
    }
}
