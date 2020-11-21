package com.chesire.nekome.user.api

/**
 * Provides functionality to map from [T] to an instance of [UserEntityMapper].
 */
interface UserEntityMapper<T> {
    /**
     * Maps instance of [T] to an instance of [UserEntity].
     */
    fun from(item: T): UserEntity
}
