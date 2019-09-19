package com.chesire.malime.flow.series.detail

import android.text.InputFilter
import android.text.Spanned

/**
 * Provides an [InputFilter] that has a [max] range for the input value.
 */
class RangeInputFilter(private val max: Int) : InputFilter {
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        val des = dest.toString()
        val src = source.toString()
        var newVal = des.substring(0, dstart) + des.substring(dend, des.length)
        newVal = newVal.substring(0, dstart) + src + newVal.substring(dstart, newVal.length)

        return newVal.toIntOrNull()?.let {
            return if (it <= max) {
                null
            } else {
                ""
            }
        } ?: ""
    }
}
