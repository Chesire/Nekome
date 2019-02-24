package com.chesire.malime.kitsu.api

import com.chesire.malime.core.Resource
import retrofit2.Response

interface ResponseParser {
    fun <T> parseResponse(response: Response<T>): Resource<T> {
        return if (response.isSuccessful) {
            response.body()?.let {
                Resource.Success(it)
            } ?: Resource.Error("Response body is null")
        } else {
            Resource.Error(response.errorBody()?.string() ?: response.message())
        }
    }
}
