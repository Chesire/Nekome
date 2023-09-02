package com.chesire.nekome.core.preferences.flags

import androidx.annotation.StringRes
import com.chesire.nekome.resources.StringResource

/**
 * Options available for the language used to display the titles.
 */
enum class TitleLanguage(val index: Int, @StringRes val stringId: Int, val key: String) {
    Canonical(0, StringResource.title_language_canonical, ""),
    English(1, StringResource.title_language_english, "en"),
    Romaji(2, StringResource.title_language_romaji, "en_jp"),
    Japanese(3, StringResource.title_language_japanese, "ja_jp");

    companion object {

        /**
         * Get [TitleLanguage] for its given [index].
         */
        fun forIndex(index: Int): TitleLanguage = values().find { it.index == index } ?: Canonical
    }
}
