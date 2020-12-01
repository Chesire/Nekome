package com.chesire.nekome.kitsu.library

import com.chesire.nekome.core.flags.SeriesStatus
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.kitsu.api.intermediaries.Links
import com.chesire.nekome.kitsu.library.dto.AddResponseDto
import com.chesire.nekome.kitsu.library.dto.DataDto
import com.chesire.nekome.kitsu.library.dto.IncludedDto
import com.chesire.nekome.kitsu.library.dto.RetrieveResponseDto
import com.chesire.nekome.library.api.LibraryDomain

/**
 * Create a [LibraryDomain] for tests.
 */
fun createLibraryDomain() =
    LibraryDomain(
        0,
        0,
        SeriesType.Anime,
        Subtype.Unknown,
        "slug",
        "synopsis",
        "title",
        SeriesStatus.Unknown,
        UserSeriesStatus.Unknown,
        0,
        0,
        ImageModel.empty,
        ImageModel.empty,
        false,
        "startDate",
        "endDate"
    )

/**
 * Create a [AddResponseDto] for tests.
 */
fun createAddResponseDto(type: SeriesType) =
    AddResponseDto(
        createDataDto(type),
        listOf(createIncludedDto(type))
    )

/**
 * Create a [RetrieveResponseDto] for tests.
 */
fun createRetrieveResponseDto(type: SeriesType, links: Links = Links()) =
    RetrieveResponseDto(
        listOf(createDataDto(type), createDataDto(type)),
        listOf(createIncludedDto(type), createIncludedDto(type)),
        links
    )

/**
 * Create a [DataDto] for tests.
 */
fun createDataDto(type: SeriesType, id: Int = 0) =
    DataDto(
        id,
        DataDto.Attributes(
            UserSeriesStatus.Unknown,
            0,
            "startedAt",
            "finishedAt"
        ),
        DataDto.Relationships(
            if (type == SeriesType.Anime) {
                DataDto.Relationships.RelationshipObject(
                    DataDto.Relationships.RelationshipObject.RelationshipData("", id)
                )
            } else {
                null
            },
            if (type == SeriesType.Manga) {
                DataDto.Relationships.RelationshipObject(
                    DataDto.Relationships.RelationshipObject.RelationshipData("", id)
                )
            } else {
                null
            },
        )
    )

/**
 * Create a [IncludedDto] for tests.
 */
@Suppress("LongParameterList")
fun createIncludedDto(
    type: SeriesType,
    id: Int = 0,
    slug: String = "slug",
    synopsis: String = "synopsis",
    canonicalTitle: String = "canonicalTitle",
    startDate: String = "startDate",
    endDate: String = "endDate",
    subtype: Subtype = Subtype.Unknown,
    seriesStatus: SeriesStatus = SeriesStatus.Unknown,
    posterImage: ImageModel = ImageModel.empty,
    coverImage: ImageModel = ImageModel.empty,
    chapterCount: Int = 0,
    episodeCount: Int = 0,
    nsfw: Boolean = false
) = IncludedDto(
    id,
    type,
    IncludedDto.Attributes(
        slug,
        synopsis,
        canonicalTitle,
        startDate,
        endDate,
        subtype,
        seriesStatus,
        posterImage,
        coverImage,
        chapterCount,
        episodeCount,
        nsfw
    )
)
