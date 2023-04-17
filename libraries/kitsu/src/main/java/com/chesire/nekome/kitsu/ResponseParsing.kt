package com.chesire.nekome.kitsu

import com.chesire.nekome.core.models.ErrorDomain
import com.chesire.nekome.datasource.auth.AuthException
import java.net.UnknownHostException
import retrofit2.Response

/**
 * Converts the current [Response] into an [ErrorDomain].
 */
fun <T> Response<T>.asError(): ErrorDomain = ErrorDomain(errorBody()?.string() ?: message(), code())

/**
 * Parses out the [Exception] providing an [ErrorDomain] for use elsewhere.
 */
fun Exception.parse(): ErrorDomain {
    return when (this) {
        is UnknownHostException -> ErrorDomain.couldNotReach
        is AuthException -> ErrorDomain.couldNotRefresh
        else -> ErrorDomain.badRequest.copy(message = toString())
    }
}
