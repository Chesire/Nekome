package com.chesire.nekome.app.discover.domain

import com.chesire.nekome.datasource.trending.TrendingDomain
import javax.inject.Inject

/**
 * Provides ability to map from instances of [TrendingDomain].
 */
class DiscoverDomainMapper @Inject constructor() {

    /**
     * Converts an instance of [TrendingDomain] into a [TrendingModel].
     */
    fun toTrendingModel(input: TrendingDomain) =
        TrendingModel(
            input.id,
            input.type,
            input.canonicalTitle,
            input.posterImage
        )
}
