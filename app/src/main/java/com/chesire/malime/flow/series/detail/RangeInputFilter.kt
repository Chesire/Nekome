package com.chesire.malime.flow.series.detail

import android.text.InputFilter
import android.text.Spanned
import timber.log.Timber

/**
 * Provides an [InputFilter] that has a [max] range for the input value.
 */
@Suppress("ReturnCount", "TooGenericExceptionCaught")
class RangeInputFilter(private val max: Int) : InputFilter {
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        if (source == null || dest == null) {
            return ""
        }
        val des = dest.toString()
        val src = source.toString()
        val newVal = try {
            val tempVal = des.substring(0, dstart) + des.substring(dend, des.length)
            tempVal.substring(0, dstart) + src + tempVal.substring(dstart, tempVal.length)
        } catch (ex: IndexOutOfBoundsException) {
            Timber.w(ex, "Values des: $dest, source: $source")
            null
        }

        return newVal?.toIntOrNull()?.let {
            return when {
                max == 0 -> null
                it <= max -> null
                else -> ""
            }
        } ?: ""
    }
}
