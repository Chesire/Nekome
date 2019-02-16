package com.chesire.malime.kitsu.adapters

import com.chesire.malime.core.flags.Subtype
import com.squareup.moshi.FromJson

@Suppress("unused")
class SubtypeAdapter {
    @FromJson
    fun subtypeFromString(subString: String): Subtype {
        return when (subString) {
            "ONA" -> Subtype.ONA
            "OVA" -> Subtype.OVA
            "TV" -> Subtype.TV
            "movie" -> Subtype.Movie
            "music" -> Subtype.Music
            "special" -> Subtype.Special
            "doujin" -> Subtype.Doujin
            "manga" -> Subtype.Manga
            "manhua" -> Subtype.Manhua
            "manhwa" -> Subtype.Manhwa
            "novel" -> Subtype.Novel
            "oel" -> Subtype.OEL
            "oneshot" -> Subtype.Oneshot
            else -> Subtype.Unknown
        }
    }
}
