package com.chesire.nekome.app.series.list.view

import android.content.Context
import android.util.AttributeSet
import com.chesire.nekome.app.series.R
import com.google.android.material.slider.Slider
import java.text.NumberFormat

/**
 * Provides an instance of [Slider] which a label formatter already setup.
 *
 * This does some internal managing of the value for the [Slider] to work with what Kitsu expects
 * the values to be.
 *  - A value of 0 will display a string to indicate no rating is performed, this allows the user to
 *    not need to provide a rating.
 *  - A value of 1 (0.5 displayed) will be skipped over, as it is invalid and Kitsu will only accept
 *    2-20.
 *  - The formatter above the [Slider] will display the values in 0.5 increments, instead of their
 *    normal value, as this matches up with how Kitsu does their ratings.
 */
class RatingSlider(context: Context, attrs: AttributeSet) : Slider(context, attrs) {
    private val noRatingString = context.getString(R.string.rating_none)

    init {
        setLabelFormatter { value ->
            if (value == 0f) {
                // If we have a value of 0, we will want to ignore it so no rating can be provided
                noRatingString
            } else {
                // Kitsu only accepts values of 2-20
                // So we allow these values and then divide the display value by 2
                val displayValue = value / 2
                NumberFormat.getInstance()
                    .apply { maximumFractionDigits = 1 }
                    .format(displayValue)
            }
        }
        addOnChangeListener { slider, value, _ ->
            // If the value is "1", skip over it because 2 is the lowest Kitsu accepts
            if (value == 1f) {
                slider.value = 2f
            }
        }
    }
}
