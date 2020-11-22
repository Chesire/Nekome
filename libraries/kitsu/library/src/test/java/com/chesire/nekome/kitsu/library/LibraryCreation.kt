package com.chesire.nekome.kitsu.library

import com.chesire.nekome.core.flags.SeriesStatus
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.kitsu.api.intermediaries.Links
import com.chesire.nekome.kitsu.library.entity.DataEntity
import com.chesire.nekome.kitsu.library.entity.IncludedEntity
import com.chesire.nekome.kitsu.library.entity.KitsuLibraryEntities
import com.chesire.nekome.kitsu.library.entity.KitsuLibraryEntity

/**
 * Create a [KitsuLibraryEntity] for tests.
 */
fun createKitsuLibraryEntity(type: SeriesType) =
    KitsuLibraryEntity(
        createDataEntity(type),
        listOf(createIncludedEntity(type))
    )

/**
 * Create a [KitsuLibraryEntities] for tests.
 */
fun createKitsuLibraryEntities(type: SeriesType, links: Links = Links()) =
    KitsuLibraryEntities(
        listOf(createDataEntity(type), createDataEntity(type)),
        listOf(createIncludedEntity(type), createIncludedEntity(type)),
        links
    )

private fun createDataEntity(type: SeriesType) =
    DataEntity(
        0,
        DataEntity.Attributes(
            UserSeriesStatus.Unknown,
            0,
            "startedAt",
            "finishedAt"
        ),
        DataEntity.Relationships(
            if (type == SeriesType.Anime) DataEntity.Relationships.RelationshipObject() else null,
            if (type == SeriesType.Manga) DataEntity.Relationships.RelationshipObject() else null,
        )
    )

private fun createIncludedEntity(type: SeriesType) =
    IncludedEntity(
        0,
        type,
        IncludedEntity.Attributes(
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
