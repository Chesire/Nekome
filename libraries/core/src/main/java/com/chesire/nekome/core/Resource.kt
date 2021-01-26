package com.chesire.nekome.core

import java.net.HttpURLConnection.HTTP_BAD_REQUEST
import java.net.HttpURLConnection.HTTP_FORBIDDEN
import java.net.HttpURLConnection.HTTP_NO_CONTENT
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED
import java.net.HttpURLConnection.HTTP_UNAVAILABLE

/**
 * Generic class to aid with receiving responses.
 */
sealed class Resource<T> {

    /**
     * Response is a success, containing [data].
     */
    class Success<T>(val data: T) : Resource<T>() {
        /**
         * Changes the current [Success] into a [Success] with [newData].
         */
        fun <U> with(newData: U) = Success(newData)
    }

    /**
     * Response was a failure, with the [msg] & [code] containing information on why.
     */
    class Error<T>(val msg: String, val code: Int = HTTP_BAD_REQUEST) : Resource<T>() {
        /**
         * Mutates this instance of [Error] of type [T] into an [Error] of type [U].
         */
        fun <U> mutate() = Error<U>(msg, code)

        companion object {

            /**
             * Constant value for when credentials are invalid.
             */
            const val InvalidCredentials = HTTP_UNAUTHORIZED

            /**
             * Creates an [Error] for a bad request.
             */
            fun <T> badRequest(body: String): Error<T> = Error(body, HTTP_BAD_REQUEST)

            /**
             * Creates an [Error] for an empty response.
             */
            fun <T> emptyResponse(): Error<T> = Error("Response body is null", HTTP_NO_CONTENT)

            /**
             * Creates an [Error] for invalid auth.
             */
            fun <T> couldNotRefresh(): Error<T> = Error("Could not refresh auth", HTTP_FORBIDDEN)

            /**
             * Creates an [Error] for being unable to reach the server.
             */
            fun <T> couldNotReach(): Error<T> = Error("Could not reach service", HTTP_UNAVAILABLE)
        }
    }
}
