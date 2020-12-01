package com.chesire.nekome.app.search

import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.core.models.SeriesModel
import com.chesire.nekome.search.api.SearchDomain
import javax.inject.Inject

/**
 * Provides ability to map instances of [SearchDomain] into [SeriesModel].
 */
class SearchDomainMapper @Inject constructor() {

    /**
     * Converts an instance of [SearchDomain] into a [SeriesModel].
     */
    fun toSeriesModel(input: SearchDomain) =
        SeriesModel(
            input.id,
            0,
            input.type,
            input.subtype,
            input.slug,
            input.synopsis,
            input.canonicalTitle,
            input.status,
            UserSeriesStatus.Unknown,
            0,
            0,
            input.posterImage ?: ImageModel.empty,
            input.coverImage ?: ImageModel.empty,
            input.nsfw,
            input.startDate ?: "",
            input.endDate ?: ""
        )
}
