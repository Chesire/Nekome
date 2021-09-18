package com.chesire.nekome.datasource.auth.local

import javax.inject.Inject

/**
 * Provides authorization for Kitsu access.
 */
class AuthProvider @Inject constructor(private val v2Auth: LocalAuthV2) {

    /**
     * Retrieve or set the current access token.
     */
    var accessToken: String
        get() = v2Auth.accessToken
        set(value) {
            v2Auth.accessToken = value
        }

    /**
     * Retrieve or set the refresh token used to get a new access token.
     */
    var refreshToken: String
        get() = v2Auth.refreshToken
        set(value) {
            v2Auth.refreshToken = value
        }

    /**
     * Clears out the current auth credentials.
     */
    fun clearAuth() = v2Auth.clear()
}
