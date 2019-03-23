package com.chesire.malime

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
    class Error<T, E>(val error: E) : AsyncState<T, E>()

    /**
     * Async request is in progress.
     */
    class Loading<T, E> : AsyncState<T, E>()
}
