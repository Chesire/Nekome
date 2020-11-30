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

private fun createDataDto(type: SeriesType) =
    DataDto(
        0,
        DataDto.Attributes(
            UserSeriesStatus.Unknown,
            0,
            "startedAt",
            "finishedAt"
        ),
        DataDto.Relationships(
            if (type == SeriesType.Anime) DataDto.Relationships.RelationshipObject() else null,
            if (type == SeriesType.Manga) DataDto.Relationships.RelationshipObject() else null,
        )
    )

private fun createIncludedDto(type: SeriesType) =
    IncludedDto(
        0,
        type,
        IncludedDto.Attributes(
            "slug",
            "synopsis",
            "canonincalTitle",
            "startDate",
            "endDate",
            Subtype.Unknown,
            SeriesStatus.Unknown,
            ImageModel.empty,
            ImageModel.empty,
            0,
            0,
            false
        )
    )
