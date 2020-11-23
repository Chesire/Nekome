package com.chesire.nekome.library

import com.chesire.nekome.core.EntityMapper
import com.chesire.nekome.core.models.SeriesModel
import com.chesire.nekome.library.api.LibraryEntity
import javax.inject.Inject

/**
 * Provides ability to map instances of [LibraryEntity] into [SeriesModel].
 */
class LibraryEntityMapper @Inject constructor() : EntityMapper<LibraryEntity, SeriesModel> {

    override fun from(input: LibraryEntity) =
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
