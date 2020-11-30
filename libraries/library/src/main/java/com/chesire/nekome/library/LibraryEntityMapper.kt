package com.chesire.nekome.library

import com.chesire.nekome.core.EntityMapper
import com.chesire.nekome.core.models.SeriesModel
import com.chesire.nekome.library.api.LibraryDomain
import javax.inject.Inject

/**
 * Provides ability to map instances of [LibraryDomain] into [SeriesModel].
 */
class LibraryEntityMapper @Inject constructor() : EntityMapper<LibraryDomain, SeriesModel> {

    override fun from(input: LibraryDomain) =
        SeriesModel(
            input.id,
            input.userId,
            input.type,
            input.subtype,
            input.slug,
            input.synopsis,
            input.title,
            input.seriesStatus,
            input.userSeriesStatus,
            input.progress,
            input.totalLength,
            input.posterImage,
            input.coverImage,
            input.nsfw,
            input.startDate,
            input.endDate
        )
}
