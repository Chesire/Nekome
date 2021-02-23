package com.chesire.nekome.helpers.creation

import com.chesire.nekome.core.flags.SeriesStatus
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.library.api.LibraryDomain

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
    rating: Int = 0,
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
    rating = rating,
    posterImage = posterImage,
    startDate = startDate,
    endDate = endDate
)
