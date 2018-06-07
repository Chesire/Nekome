package com.chesire.malime.core

enum class ItemType(
    val internalId: Int,
    val text: String
) {
    Unknown(0, "unknown"),
    Anime(1, "anime"),
    Manga(2, "manga");

    companion object {
        fun getTypeForString(inputString: String): ItemType {
            return ItemType.values().find { it.text == inputString } ?: Unknown
        }

        fun getTypeForInternalId(id: Int): ItemType {
            return ItemType.values().find { it.internalId == id } ?: Unknown
        }
    }
}