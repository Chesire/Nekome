package com.chesire.nekome.core

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
        fun <U> with(newData: U) = Success<U>(newData)
    }

    /**
     * Response was a failure, with the [msg] & [code] containing information on why.
     */
    class Error<T>(val msg: String, val code: Int = GenericError) : Resource<T>() {
        /**
         * Morphs the [Error] of type [T] into an [Error] of type [U].
         */
        fun <U> morph() = Error<U>(msg, code)

        companion object {
            /**
             * Generic error.
             */
            const val GenericError = 200

            /**
             * Empty body() object.
             */
            const val EmptyBody = 204

            /**
             * Could not refresh the access token.
             */
            const val CouldNotRefresh = 401

            /**
             * Unable to reach the server.
             */
            const val CouldNotReach = 503

            /**
             * Creates an [Error] for an empty response.
             */
            fun <T> emptyResponse(): Error<T> = Error("Response body is null", EmptyBody)

            /**
             * Creates an [Error] for being unable to reach the server.
             */
            fun <T> couldNotReach(): Error<T> = Error("Could not reach service", CouldNotReach)
        }
    }
}
