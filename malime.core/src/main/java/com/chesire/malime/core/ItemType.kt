package com.chesire.malime.core

enum class ItemType {
    Anime,
    Manga;

    companion object {
        fun getTypeForString(inputString: String) {
            when (inputString.toLowerCase()) {
                "anime" -> Anime
                "manga" -> Manga
                else -> Anime // guess its an anime?
            }
        }
    }
}