package com.chesire.nekome.search.api

/**
 * Provides functionality to map from [T] to an instance of [SearchEntity].
 */
interface SearchEntityMapper<T> {
    /**
     * Maps instance of [T] to an instance of [SearchEntity].
     */
    fun from(item: T): SearchEntity
}
