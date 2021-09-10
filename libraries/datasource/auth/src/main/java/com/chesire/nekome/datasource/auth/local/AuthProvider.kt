package com.chesire.nekome.datasource.auth.local

import javax.inject.Inject

/**
 * Provides authorization for Kitsu access.
 */
class AuthProvider @Inject constructor(
    private val v1Auth: LocalAuthV1,
    private val v2Auth: LocalAuthV2
) {

    init {
        migrateFromV1ToV2()
    }

    var accessToken: String
        get() = v2Auth.accessToken
        set(value) {
            v2Auth.accessToken = value
        }

    var refreshToken: String
        get() = v2Auth.refreshToken
        set(value) {
            v2Auth.refreshToken = value
        }

    fun clearAuth() = v2Auth.clear()

    private fun migrateFromV1ToV2() {
        if (!v1Auth.hasCredentials) {
            return
        }

        val v1AccessToken = v1Auth.accessToken
        val v1RefreshToken = v1Auth.refreshToken

        v2Auth.accessToken = v1AccessToken
        v2Auth.refreshToken = v1RefreshToken
        v1Auth.clear()
    }
}
