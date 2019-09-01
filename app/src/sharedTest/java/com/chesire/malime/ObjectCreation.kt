package com.chesire.malime

import com.chesire.malime.server.flags.SeriesStatus
import com.chesire.malime.server.flags.SeriesType
import com.chesire.malime.server.flags.Subtype
import com.chesire.malime.server.flags.UserSeriesStatus
import com.chesire.malime.server.models.ImageModel
import com.chesire.malime.server.models.SeriesModel

fun createSeriesModel(
    id: Int = 999,
    userId: Int = 999,
    seriesType: SeriesType = SeriesType.Anime,
    subType: Subtype = Subtype.TV,
    slug: String = "slug",
    title: String = "title",
    seriesStatus: SeriesStatus = SeriesStatus.Current,
    userSeriesStatus: UserSeriesStatus = UserSeriesStatus.Current,
    progress: Int = 0,
    totalLength: Int = 0,
    posterImage: ImageModel = ImageModel.empty,
    coverImage: ImageModel = ImageModel.empty,
    nsfw: Boolean = false,
    startDate: String = "",
    endDate: String = ""
) = SeriesModel(
    id,
    userId,
    seriesType,
    subType,
    slug,
    title,
    seriesStatus,
    userSeriesStatus,
    progress,
    totalLength,
    posterImage,
    coverImage,
    nsfw,
    startDate,
    endDate
)
