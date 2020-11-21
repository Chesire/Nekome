package com.chesire.nekome.core

/**
 * Maps entities from the type [T] to the type [U].
 */
interface EntityMapper<T, U> {
    /**
     * Maps the [input] into type [U].
     */
    fun from(input: T): U
}
