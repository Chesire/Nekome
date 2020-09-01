package com.chesire.nekome.kitsu

import com.chesire.nekome.server.Resource
import retrofit2.Response
import java.net.UnknownHostException

/**
 * Parse out a [Response] into a [Resource] object.
 */
internal fun <T> Response<T>.parse(): Resource<T> {
    return if (isSuccessful) {
        body()?.let {
            Resource.Success(it)
        } ?: emptyResponseError()
    } else {
        parseError()
    }
}

/**
 * Parses out the [Response] into a [Resource.Error] object.
 */
internal fun <T> Response<T>.parseError(): Resource.Error<T> {
    return Resource.Error(
        errorBody()?.string() ?: message(),
        code()
    )
}

/**
 * Generates a [Resource] for an empty [Response].
 */
internal fun <T> emptyResponseError(): Resource.Error<T> =
    Resource.Error("Response body is null", 204)

/**
 * Parses out the [Exception] providing a [Resource] for use elsewhere.
 */
internal fun <T> Exception.parse(): Resource.Error<T> {
    return if (this is UnknownHostException) {
        Resource.Error("Could not reach service", 503)
    } else {
        Resource.Error(toString(), 400)
    }
}
