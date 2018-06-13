package com.chesire.malime.kitsu.models

data class AddItemResponse(
    val data: LibraryItem,
    val included: List<LibraryItem>
)