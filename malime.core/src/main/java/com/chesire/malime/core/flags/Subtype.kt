package com.chesire.malime.core.flags

enum class Subtype(
    val internalId: Int,
    val malId: Int, // this might be a string in MAL?
    val kitsuString: String
) {
    Unknown(0, 0, "unknown"),

    // :Anime
    ONA(1, 1, "ONA"),
    OVA(2, 2, "OVA"),
    TV(3, 3, "TV"),
    Movie(4, 4, "movie"),
    Music(5, 5, "music"),
    Special(6, 6, "special"),

    // :Manga
    Doujin(7, 7, "doujin"),
    Manga(8, 8, "manga"),
    Manhua(9, 9, "manhua"),
    Manhwa(10, 10, "manhwa"),
    Novel(11, 11, "novel"),
    OEL(12, 12, "oel"),
    Oneshot(13, 13, "oneshot");

    companion object {
        fun getSubtypeForKitsuString(inputString: String): Subtype {
            return Subtype.values().find { it.kitsuString == inputString } ?: Unknown
        }

        fun getSubtypeForMalId(id: Int): Subtype {
            return Subtype.values().find { it.malId == id } ?: Unknown
        }

        fun getSubtypeForInternalId(id: Int): Subtype {
            return Subtype.values().find { it.internalId == id } ?: Unknown
        }
    }
}