package com.chesire.nekome.helpers

import com.chesire.nekome.datasource.auth.local.AuthProvider

/**
 * Tells the [AuthProvider] that a user is logged in, used to skip login.
 */
fun AuthProvider.login() {
    accessToken = "fakeAccessToken"
}

/**
 * Tells the [AuthProvider] that a user is logged out, used to enter login.
 */
fun AuthProvider.logout() {
    accessToken = ""
}
