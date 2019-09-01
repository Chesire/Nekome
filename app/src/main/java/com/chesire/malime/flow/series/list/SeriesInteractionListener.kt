package com.chesire.malime.flow.series.list

import android.widget.ImageView
import com.chesire.malime.core.models.SeriesModel

interface SeriesInteractionListener {
    /**
     * Executed when a series has been selected.
     */
    fun seriesSelected(imageView: ImageView, model: SeriesModel)

    /**
     * Executed when the "Plus one" button has been pressed, the [callback] is then fired on
     * completion.
     */
    fun onPlusOne(model: SeriesModel, callback: () -> Unit)
}
