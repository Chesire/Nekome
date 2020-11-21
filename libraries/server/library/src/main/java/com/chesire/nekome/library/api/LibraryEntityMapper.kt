package com.chesire.nekome.library.api

/**
 * Provides functionality to map from [T] to an instance of [LibraryEntity].
 */
interface LibraryEntityMapper<T> {
    /**
     * Maps instance of [T] to an instance of [LibraryEntity].
     */
    fun from(item: T): LibraryEntity
}
