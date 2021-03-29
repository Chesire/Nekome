package com.chesire.nekome.app.series.list

import android.widget.ImageView
import com.chesire.nekome.datasource.series.SeriesDomain

/**
 * Listener for events happening when interacting with the series list.
 */
interface SeriesInteractionListener {

    /**
     * Executed when a series has been selected.
     */
    fun seriesSelected(imageView: ImageView, model: SeriesDomain)

    /**
     * Executed when the "Plus one" button has been pressed, the [callback] is then fired on
     * completion.
     */
    fun onPlusOne(model: SeriesDomain, callback: () -> Unit)

    /**
     * Executed when a series has been chosen for deletion, The [callback] is fired on completion
     * with a value of true if deletion is confirmed, or false if it is cancelled.
     */
    fun seriesDelete(model: SeriesDomain, callback: (Boolean) -> Unit)
}
