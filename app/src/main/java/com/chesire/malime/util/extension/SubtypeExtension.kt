package com.chesire.malime.util.extension

import android.content.Context
import com.chesire.malime.R
import com.chesire.malime.core.flags.Subtype

@Suppress("ComplexMethod")
fun Subtype.getString(context: Context): String {
    val stringId = when (this) {
        Subtype.ONA -> R.string.series_subtype_ona
        Subtype.OVA -> R.string.series_subtype_ova
        Subtype.TV -> R.string.series_subtype_tv
        Subtype.Movie -> R.string.series_subtype_movie
        Subtype.Music -> R.string.series_subtype_music
        Subtype.Special -> R.string.series_subtype_special
        Subtype.Doujin -> R.string.series_subtype_doujin
        Subtype.Manga -> R.string.series_subtype_manga
        Subtype.Manhua -> R.string.series_subtype_manhua
        Subtype.Manhwa -> R.string.series_subtype_manhwa
        Subtype.Novel -> R.string.series_subtype_novel
        Subtype.OEL -> R.string.series_subtype_oel
        Subtype.Oneshot -> R.string.series_subtype_oneshot
        Subtype.Unknown -> R.string.series_subtype_unknown
    }

    return context.getString(stringId)
}