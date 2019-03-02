package com.chesire.malime.kitsu

import com.chesire.malime.core.Resource
import retrofit2.Response
import java.net.UnknownHostException

internal fun <T> Response<T>.parse(): Resource<T> {
    return if (isSuccessful) {
        body()?.let {
            Resource.Success(it)
        } ?: emptyResponseError()
    } else {
        parseError()
    }
}

internal fun <T> Response<T>.parseError(): Resource.Error<T> {
    return Resource.Error(
        errorBody()?.string() ?: message(),
        code()
    )
}

internal fun <T> emptyResponseError(): Resource.Error<T> =
    Resource.Error("Response body is null", 204)

internal fun <T> Exception.parse(): Resource.Error<T> {
    return when (this) {
        is UnknownHostException -> Resource.Error("Could not reach service", 503)
        else -> Resource.Error("Unknown error encountered", 400)
    }
}
