package com.chesire.nekome.app.series.list.view

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.slider.Slider
import java.text.NumberFormat

/**
 * Provides an instance of [Slider] which a label formatter already setup.
 */
class RatingSlider(context: Context, attrs: AttributeSet) : Slider(context, attrs) {
    init {
        setLabelFormatter { value ->
            // Kitsu accepts values of 2-20
            // So we allow these values and then divide the display value by 2
            val displayValue = value / 2
            NumberFormat.getInstance()
                .apply { maximumFractionDigits = 1 }
                .format(displayValue)
        }
    }
}
