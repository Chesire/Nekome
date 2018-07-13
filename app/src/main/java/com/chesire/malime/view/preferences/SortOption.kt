package com.chesire.malime.view.preferences

import android.content.Context
import android.support.annotation.StringRes
import com.chesire.malime.R

enum class SortOption(
    val id: Int,
    @StringRes
    val stringId: Int
) {
    Default(0, R.string.sort_choice_default),
    Title(1, R.string.sort_choice_title),
    StartDate(2, R.string.sort_choice_start_date),
    EndDate(3, R.string.sort_choice_end_date);

    companion object {
        fun getOptionFor(id: Int) = SortOption.values().find { it.id == id } ?: Default

        fun getOptionsStrings(context: Context) =
            SortOption.values().map { context.getString(it.stringId) }.toTypedArray()
    }
}
