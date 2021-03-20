package com.chesire.nekome.testing

import com.chesire.nekome.core.flags.SeriesStatus
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.Service
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.database.entity.SeriesEntity
import com.chesire.nekome.database.entity.UserEntity
import com.chesire.nekome.datasource.user.UserDomain
import com.chesire.nekome.library.SeriesDomain

/**
 * Creates a new instance of [SeriesDomain] with default data.
 */
@Suppress("LongParameterList", "LongMethod")
fun createSeriesDomain(
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
    rating: Int = 0,
    posterImage: ImageModel = ImageModel.empty,
    startDate: String = "",
    endDate: String = ""
) = SeriesDomain(
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
    rating,
    posterImage,
    startDate,
    endDate
)

/**
 * Creates a new instance of [SeriesEntity] with default data.
 */
@Suppress("LongParameterList", "LongMethod")
fun createSeriesEntity(
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
    rating: Int = 0,
    posterImage: ImageModel = ImageModel.empty,
    startDate: String = "",
    endDate: String = ""
) = SeriesEntity(
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
    rating,
    posterImage,
    startDate,
    endDate
)

/**
 * Creates a new instance of [UserDomain] with default data.
 */
fun createUserDomain(
    userId: Int = 0,
    name: String = "name",
    avatar: ImageModel = ImageModel.empty,
    coverImage: ImageModel = ImageModel.empty,
    service: Service = Service.Kitsu
) = UserDomain(userId, name, avatar, coverImage, service)

/**
 * Creates a new instance of [UserEntity] with default data.
 */
fun createUserEntity(
    userId: Int = 0,
    name: String = "name",
    avatar: ImageModel = ImageModel.empty,
    coverImage: ImageModel = ImageModel.empty,
    service: Service = Service.Kitsu
) = UserEntity(userId, name, avatar, coverImage, service)

/**
 * Creates a new instance of [ImageModel] with default data.
 */
fun createImageModel(
    tiny: ImageModel.ImageData = ImageModel.ImageData.empty,
    small: ImageModel.ImageData = ImageModel.ImageData.empty,
    medium: ImageModel.ImageData = ImageModel.ImageData.empty,
    large: ImageModel.ImageData = ImageModel.ImageData.empty
) = ImageModel(tiny, small, medium, large)
