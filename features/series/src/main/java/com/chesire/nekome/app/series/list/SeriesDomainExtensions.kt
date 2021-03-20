package com.chesire.nekome.app.series.list

import com.chesire.nekome.datasource.series.SeriesDomain

/**
 * Property that checks if the length of a [SeriesDomain] is known.
 */
val SeriesDomain.lengthKnown: Boolean
    get() = totalLength != 0
