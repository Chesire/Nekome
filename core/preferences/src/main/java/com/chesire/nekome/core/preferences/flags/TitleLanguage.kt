package com.chesire.nekome.core.preferences.flags

import androidx.annotation.StringRes
import com.chesire.nekome.core.preferences.R

/**
 * Options available for the language used to display the titles.
 */
enum class TitleLanguage(val index: Int, @StringRes val stringId: Int) {
    Canonical(0, R.string.title_language_canonical),
    English(1, R.string.title_language_english),
    Romaji(2, R.string.title_language_romaji),
    Japanese(3, R.string.title_language_japanese);

    companion object {

        /**
         * Get [TitleLanguage] for its given [index].
         */
        fun forIndex(index: Int): TitleLanguage = values().find { it.index == index } ?: Canonical
    }
}
