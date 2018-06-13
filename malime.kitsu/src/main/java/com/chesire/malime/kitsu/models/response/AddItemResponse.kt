package com.chesire.malime.kitsu.models.response

import com.chesire.malime.kitsu.models.LibraryItem

data class AddItemResponse(
    val data: LibraryItem,
    val included: List<LibraryItem>
)