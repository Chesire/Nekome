package com.chesire.nekome.app.search.domain

import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.search.api.SearchDomain
import javax.inject.Inject

/**
 * Provides ability to map from instances of [SearchDomain].
 */
class SearchDomainMapper @Inject constructor() {

    /**
     * Converts an instance of [SearchDomain] into a [SearchModel].
     */
    fun toSearchModel(input: SearchDomain) =
        SearchModel(
            input.id,
            input.type,
            input.synopsis,
            input.canonicalTitle,
            input.subtype,
            input.posterImage ?: ImageModel.empty
        )
}
