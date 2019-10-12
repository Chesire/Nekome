package com.chesire.malime.testing

import com.chesire.malime.core.flags.SeriesStatus
import com.chesire.malime.core.flags.SeriesType
import com.chesire.malime.core.flags.Service
import com.chesire.malime.core.flags.Subtype
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.ImageModel
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.core.models.UserModel

/**
 * Creates a new instance of [SeriesModel] with default data.
 */
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

/**
 * Creates a new instance of [UserModel] with default data.
 */
fun createUserModel(
    userId: Int = 0,
    name: String = "name",
    avatar: ImageModel = ImageModel.empty,
    coverImage: ImageModel = ImageModel.empty,
    service: Service = Service.Kitsu
) = UserModel(userId, name, avatar, coverImage, service)
