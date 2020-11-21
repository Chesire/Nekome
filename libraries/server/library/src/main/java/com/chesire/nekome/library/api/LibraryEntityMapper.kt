package com.chesire.nekome.library.api

interface LibraryEntityMapper<T> {
    fun mapFromLibraryEntity(entity: LibraryEntity): T
    fun mapToLibraryEntity(item: T): LibraryEntity
}
