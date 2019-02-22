package com.chesire.malime.kitsu.adapters

import com.chesire.malime.core.flags.Subtype
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

private const val ONA = "ONA"
private const val OVA = "OVA"
private const val TV = "TV"
private const val MOVIE = "movie"
private const val MUSIC = "music"
private const val SPECIAL = "special"
private const val DOUJIN = "doujin"
private const val MANGA = "manga"
private const val MANHUA = "manhua"
private const val MANHWA = "manhwa"
private const val NOVEL = "novel"
private const val OEL = "oel"
private const val ONESHOT = "oneshot"
private const val UNKNOWN = "unknown"

class SubtypeAdapter {
    @Suppress("ComplexMethod")
    @FromJson
    fun subtypeFromString(subString: String): Subtype {
        return when (subString) {
            ONA -> Subtype.ONA
            OVA -> Subtype.OVA
            TV -> Subtype.TV
            MOVIE -> Subtype.Movie
            MUSIC -> Subtype.Music
            SPECIAL -> Subtype.Special
            DOUJIN -> Subtype.Doujin
            MANGA -> Subtype.Manga
            MANHUA -> Subtype.Manhua
            MANHWA -> Subtype.Manhwa
            NOVEL -> Subtype.Novel
            OEL -> Subtype.OEL
            ONESHOT -> Subtype.Oneshot
            else -> Subtype.Unknown
        }
    }

    @Suppress("ComplexMethod")
    @ToJson
    fun subtypeToString(subtype: Subtype): String {
        return when (subtype) {
            Subtype.ONA -> ONA
            Subtype.OVA -> OVA
            Subtype.TV -> TV
            Subtype.Movie -> MOVIE
            Subtype.Music -> MUSIC
            Subtype.Special -> SPECIAL
            Subtype.Doujin -> DOUJIN
            Subtype.Manga -> MANGA
            Subtype.Manhua -> MANHUA
            Subtype.Manhwa -> MANHWA
            Subtype.Novel -> NOVEL
            Subtype.OEL -> OEL
            Subtype.Oneshot -> ONESHOT
            else -> UNKNOWN
        }
    }
}
