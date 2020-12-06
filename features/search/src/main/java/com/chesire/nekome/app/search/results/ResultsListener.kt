package com.chesire.nekome.app.search.results

import com.chesire.nekome.app.search.domain.SearchModel

/**
 * Provides listener interface for interacting with the results list.
 */
interface ResultsListener {

    /**
     * Executed when the "Track" button has been pressed, the [callback] is then fired on
     * completion.
     */
    fun onTrack(model: SearchModel, callback: () -> Unit)
}
