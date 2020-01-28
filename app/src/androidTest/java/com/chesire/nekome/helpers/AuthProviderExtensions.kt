package com.chesire.nekome.helpers

import com.chesire.nekome.kitsu.AuthProvider

/**
 * Tells the [AuthProvider] that a user is logged in, used to skip login.
 */
fun AuthProvider.login() {
    accessToken = "fakeAccessToken"
}
