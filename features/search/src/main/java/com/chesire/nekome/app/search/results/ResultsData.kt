package com.chesire.nekome.app.search.results

import com.chesire.nekome.seriesflags.SeriesType

/**
 * Aids with storing the required details that the [ResultsFragment] can send on to the
 * [ResultsViewModel] for adding series.
 */
data class ResultsData(val id: Int, val type: SeriesType)
