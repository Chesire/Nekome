package com.chesire.nekome.app.search

import com.chesire.nekome.core.flags.SeriesType

/**
 * Aids with storing the required details that the [SearchFragment] can send on to the
 * [SearchViewModel] for search requests.
 */
data class SearchData(val title: String, val seriesType: SeriesType)
