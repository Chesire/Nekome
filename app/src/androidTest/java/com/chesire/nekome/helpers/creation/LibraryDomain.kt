package com.chesire.nekome.helpers.creation

import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.library.api.LibraryDomain
import com.chesire.nekome.seriesflags.SeriesStatus
import com.chesire.nekome.seriesflags.SeriesType
import com.chesire.nekome.seriesflags.Subtype
import com.chesire.nekome.seriesflags.UserSeriesStatus

/**
 * Creates a new [LibraryDomain].
 */
@Suppress("LongParameterList")
fun createLibraryDomain(
    id: Int = 0,
    userId: Int = 0,
    seriesType: SeriesType = SeriesType.Anime,
    subtype: Subtype = Subtype.Unknown,
    slug: String = "slug",
    title: String = "title",
    seriesStatus: SeriesStatus = SeriesStatus.Unknown,
    userSeriesStatus: UserSeriesStatus = UserSeriesStatus.Unknown,
    progress: Int = 0,
    totalLength: Int = 0,
    posterImage: ImageModel = ImageModel.empty,
    startDate: String = "startDate",
    endDate: String = "endDate"
) = LibraryDomain(
    id = id,
    userId = userId,
    type = seriesType,
    subtype = subtype,
    slug = slug,
    title = title,
    seriesStatus = seriesStatus,
    userSeriesStatus = userSeriesStatus,
    progress = progress,
    totalLength = totalLength,
    posterImage = posterImage,
    startDate = startDate,
    endDate = endDate
)
