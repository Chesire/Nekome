package com.chesire.nekome.trending.api

interface TrendingEntityMapper<T> {
    fun mapFromTrendingEntity(entity: TrendingEntity): T
    fun mapToTrendingEntity(item: T): TrendingEntity
}
