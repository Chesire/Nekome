package com.chesire.nekome.trending.api

/**
 * Provides functionality to map from [T] to an instance of [TrendingEntity].
 */
interface TrendingEntityMapper<T> {
    /**
     * Maps instance of [T] to an instance of [TrendingEntity].
     */
    fun from(item: T): TrendingEntity
}
