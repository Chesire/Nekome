package com.chesire.nekome.kitsu.library

import com.chesire.nekome.core.EntityMapper
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.kitsu.library.entity.DataEntity
import com.chesire.nekome.kitsu.library.entity.IncludedEntity
import com.chesire.nekome.kitsu.library.entity.KitsuLibraryEntity
import com.chesire.nekome.library.api.LibraryEntity
import javax.inject.Inject

/**
 * Provides ability to map instances of [KitsuLibraryEntity] into [LibraryEntity].
 */
class KitsuLibraryEntityMapper @Inject constructor() :
    EntityMapper<KitsuLibraryEntity, LibraryEntity?> {

    override fun from(input: KitsuLibraryEntity): LibraryEntity? {
        val id = input.data.relationships.anime?.data?.id
            ?: input.data.relationships.manga?.data?.id
            ?: return null

        return input.included
            .find { it.id == id }
            ?.let { included ->
                createLibraryEntity(included, input.data)
            }
    }

    private fun createLibraryEntity(included: IncludedEntity, data: DataEntity) =
        LibraryEntity(
            included.id,
            data.id,
            included.type,
            included.attributes.subtype,
            included.attributes.slug,
            included.attributes.synopsis,
            included.attributes.canonicalTitle,
            included.attributes.status,
            data.attributes.status,
            data.attributes.progress,
            included.attributes.episodeCount ?: included.attributes.chapterCount ?: 0,
            included.attributes.posterImage ?: ImageModel.empty,
            included.attributes.coverImage ?: ImageModel.empty,
            included.attributes.nsfw,
            included.attributes.startDate ?: "",
            included.attributes.endDate ?: ""
        )
}
