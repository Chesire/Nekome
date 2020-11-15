package com.chesire.nekome.core

/**
 * Generic class to aid with receiving responses.
 */
sealed class Resource<T> {
    /**
     * Response is a success, containing [data].
     */
    class Success<T>(val data: T) : Resource<T>()

    /**
     * Response was a failure, with the [msg] & [code] containing information on why.
     */
    class Error<T>(val msg: String, val code: Int = GenericError) : Resource<T>() {
        companion object {
            /**
             * Generic error.
             */
            const val GenericError = 200

            /**
             * Could not refresh the access token.
             */
            const val CouldNotRefresh = 401
        }
    }
}
