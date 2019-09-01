package com.chesire.malime.flow.series.search

import com.chesire.malime.server.models.SeriesModel

interface SearchInteractionListener {
    /**
     * Executed when the add series button is pressed.
     */
    fun addSeries(model: SeriesModel)
}
