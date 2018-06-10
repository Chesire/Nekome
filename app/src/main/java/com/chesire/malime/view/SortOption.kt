package com.chesire.malime.view

import android.support.annotation.StringRes
import com.chesire.malime.MalimeApplication
import com.chesire.malime.R

enum class SortOption(
    val id: Int,
    @StringRes
    private val stringId: Int
) {
    Default(0, R.string.sort_choice_default),
    Title(1, R.string.sort_choice_title),
    StartDate(2, R.string.sort_choice_start_date),
    EndDate(3, R.string.sort_choice_end_date);

    override fun toString(): String {
        return MalimeApplication.context.getString(stringId)
    }

    companion object {
        fun getAllOptions(): Array<CharSequence> {
            return values().map { it.toString() }.toTypedArray()
        }

        fun getOptionFor(id: Int): SortOption {
            return SortOption.values().find { it.id == id } ?: Default
        }
    }
}