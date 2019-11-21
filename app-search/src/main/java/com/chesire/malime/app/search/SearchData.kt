package com.chesire.malime.app.search

import com.chesire.malime.core.flags.SeriesType

/**
 * Aids with storing the required details that the [SearchFragment] can send on to the
 * [SearchViewModel] for search requests.
 */
data class SearchData(val title: String, val seriesType: SeriesType)
