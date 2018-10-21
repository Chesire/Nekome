package com.chesire.malime

import com.chesire.malime.core.flags.ItemType
import com.chesire.malime.core.flags.SeriesStatus
import com.chesire.malime.core.flags.Subtype
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.MalimeModel

fun getMalimeModel(
    seriesId: Int,
    userSeriesId: Int = 0,
    type: ItemType = ItemType.Unknown,
    subtype: Subtype = Subtype.Unknown,
    slug: String = "SERIES-SLUG",
    title: String = "",
    seriesStatus: SeriesStatus = SeriesStatus.Unknown,
    userSeriesStatus: UserSeriesStatus = UserSeriesStatus.Unknown,
    progress: Int = 0,
    totalLength: Int = 0,
    posterImage: String = "",
    coverImage: String = "",
    nsfw: Boolean = false,
    startDate: String = "0000-00-00",
    endDate: String = "0000-00-00"
): MalimeModel {
    return MalimeModel(
        seriesId = seriesId,
        userSeriesId = userSeriesId,
        type = type,
        subtype = subtype,
        slug = slug,
        title = title,
        seriesStatus = seriesStatus,
        userSeriesStatus = userSeriesStatus,
        progress = progress,
        totalLength = totalLength,
        posterImage = posterImage,
        coverImage = coverImage,
        nsfw = nsfw,
        startDate = startDate,
        endDate = endDate
    )
}