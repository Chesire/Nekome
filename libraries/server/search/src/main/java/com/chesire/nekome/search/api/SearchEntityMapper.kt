package com.chesire.nekome.search.api

interface SearchEntityMapper<T> {
    fun mapFromSearchEntity(entity: SearchEntity): T
    fun mapToSearchEntity(item: T): SearchEntity
}
