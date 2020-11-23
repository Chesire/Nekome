package com.chesire.nekome.kitsu

import com.chesire.nekome.core.Resource
import retrofit2.Response
import java.net.UnknownHostException

/**
 * Parse out a [Response] into a [Resource] object.
 */
fun <T> Response<T>.parse(): Resource<T> {
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
fun <T> Response<T>.parseError(): Resource.Error<T> {
    return Resource.Error(
        errorBody()?.string() ?: message(),
        code()
    )
}

/**
 * Generates a [Resource] for an empty [Response].
 */
fun <T> emptyResponseError(): Resource.Error<T> = Resource.Error.emptyResponse()

/**
 * Converts the current [Response] into a [Resource.Error] with a data type of [U] instead of [T].
 */
fun <T, U> Response<T>.asError() = Resource.Error<U>(
    errorBody()?.string() ?: message(),
    code()
)

/**
 * Parses out the [Exception] providing a [Resource] for use elsewhere.
 */
fun <T> Exception.parse(): Resource.Error<T> {
    return if (this is UnknownHostException) {
        Resource.Error.couldNotReach()
    } else {
        Resource.Error(toString(), 400)
    }
}
