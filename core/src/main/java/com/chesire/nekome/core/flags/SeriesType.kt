package com.chesire.nekome.core.flags

/**
 * All possible types of a series.
 */
enum class SeriesType(val id: Int) {
    Unknown(-1),
    Anime(0),
    Manga(1);

    companion object {
        /**
         * Gets the series typed based on its id.
         */
        fun forId(typeId: Int): SeriesType = values().find { it.id == typeId } ?: Unknown
    }
}
