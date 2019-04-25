package com.chesire.malime.flow.series.list.anime

import android.widget.ImageView
import com.chesire.malime.core.models.SeriesModel

interface AnimeInteractionListener {
    /**
     * Executed when an anime series has been selected.
     */
    fun animeSelected(imageView: ImageView, model: SeriesModel)

    /**
     * Executed when the "Plus one" button has been pressed.
     */
    fun onPlusOne(model: SeriesModel)
}
