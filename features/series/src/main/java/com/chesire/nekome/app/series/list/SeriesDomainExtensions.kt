package com.chesire.nekome.app.series.list

import com.chesire.nekome.library.SeriesDomain

/**
 * Property that checks if the length of a [SeriesDomain] is known.
 */
val SeriesDomain.lengthKnown: Boolean
    get() = totalLength != 0
