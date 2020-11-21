package com.chesire.nekome.auth.api

/**
 * Provides functionality to map from [T] to an instance of [AuthEntity].
 */
interface AuthEntityMapper<T> {
    /**
     * Maps instance of [T] to an instance of [AuthEntity].
     */
    fun from(item: T): AuthEntity
}
