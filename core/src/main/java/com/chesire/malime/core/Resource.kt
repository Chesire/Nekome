package com.chesire.malime.core

sealed class Resource<T> {
    class Success<T>(val data: T) : Resource<T>()
    class Error<T>(val msg: String, val code: Int = GenericError) : Resource<T>() {
        companion object {
            const val GenericError = 200
            const val CouldNotRefresh = 401
        }
    }
}
