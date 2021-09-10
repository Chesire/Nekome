package com.chesire.nekome.datasource.auth.local

internal interface LocalAuth {

    /**
     * Checks if there are credentials for this local auth.
     */
    val hasCredentials: Boolean

    /**
     * Access token to put into the api requests.
     */
    var accessToken: String

    /**
     * Refresh token to request a new access token.
     */
    var refreshToken: String

    /**
     * Clears out any currently stored auth tokens for this local auth.
     */
    fun clear()
}
