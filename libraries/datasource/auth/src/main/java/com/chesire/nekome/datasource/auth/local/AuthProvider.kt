package com.chesire.nekome.datasource.auth.local

import javax.inject.Inject

/**
 * Provides authorization for Kitsu access.
 */
class AuthProvider @Inject constructor(private val auth: LocalAuth) {

    /**
     * Retrieve or set the current access token.
     */
    var accessToken: String
        get() = auth.accessToken
        set(value) {
            auth.accessToken = value
        }

    /**
     * Retrieve or set the refresh token used to get a new access token.
     */
    var refreshToken: String
        get() = auth.refreshToken
        set(value) {
            auth.refreshToken = value
        }

    /**
     * Clears out the current auth credentials.
     */
    fun clearAuth() = auth.clear()
}
