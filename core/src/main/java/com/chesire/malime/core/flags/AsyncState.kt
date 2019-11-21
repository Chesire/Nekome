package com.chesire.malime.core.flags

/**
 * Provides a container for async requests to communicate via live data.
 */
sealed class AsyncState<T, E> {
    /**
     * Async request has completed, and was successful.
     */
    class Success<T, E>(val data: T) : AsyncState<T, E>()

    /**
     * Async request has completed, and produced an error.
     */
    class Error<T, E>(val data: T?, val error: E) : AsyncState<T, E>() {
        /**
         * Constructor which uses null for the data field.
         */
        constructor(error: E) : this(null, error)
    }

    /**
     * Async request is in progress.
     */
    class Loading<T, E> : AsyncState<T, E>()
}
