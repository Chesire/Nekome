package com.chesire.nekome.helpers.creation

import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.datasource.search.SearchDomain

/**
 * Creates a new [SearchDomain].
 */
@Suppress("LongParameterList")
fun createSearchDomain(
    id: Int = 0,
    type: SeriesType = SeriesType.Anime,
    synopsis: String = "synopsis",
    canonicalTitle: String = "canonicalTitle",
    subtype: Subtype = Subtype.TV,
    posterImage: ImageModel = ImageModel.empty
) = SearchDomain(
    id,
    type,
    synopsis,
    canonicalTitle,
    subtype,
    posterImage
)
