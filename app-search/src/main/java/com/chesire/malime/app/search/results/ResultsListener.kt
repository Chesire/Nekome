package com.chesire.malime.app.search.results

import com.chesire.malime.core.models.SeriesModel

/**
 * Provides listener interface for interacting with the results list.
 */
interface ResultsListener {

    /**
     * Executed when the "Track" button has been pressed, the [callback] is then fired on
     * completion.
     */
    fun onTrack(model: SeriesModel, callback: () -> Unit)
}
