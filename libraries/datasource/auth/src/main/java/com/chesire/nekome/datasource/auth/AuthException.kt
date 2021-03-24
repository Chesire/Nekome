package com.chesire.nekome.datasource.auth

import java.io.IOException

/**
 * Exception thrown when the auth token is unable to be refreshed.
 */
class AuthException : IOException()
// IOException must be extended or the Retrofit interceptors will crash
