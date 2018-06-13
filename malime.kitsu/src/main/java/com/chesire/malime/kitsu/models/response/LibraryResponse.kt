package com.chesire.malime.kitsu.models.response

import com.chesire.malime.kitsu.models.LibraryItem

data class LibraryResponse(
    val data: Array<LibraryItem>,
    val included: Array<LibraryItem>,
    val meta: Map<String, Int>,
    val links: Map<String, String>
)