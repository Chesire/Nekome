package com.chesire.nekome.kitsu.library

import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.kitsu.library.entity.KitsuLibraryEntity
import com.chesire.nekome.library.api.LibraryEntity
import com.chesire.nekome.library.api.LibraryEntityMapper
import javax.inject.Inject

class KitsuLibraryEntityMapper @Inject constructor() : LibraryEntityMapper<KitsuLibraryEntity> {

    override fun mapFromLibraryEntity(entity: LibraryEntity): KitsuLibraryEntity {
        error("Unneeded currently")
    }

    override fun mapToLibraryEntity(item: KitsuLibraryEntity): LibraryEntity {
        val id = item.data.relationships.anime?.data?.id
            ?: item.data.relationships.manga?.data?.id
            ?: error("")

        return item.included
            .find { it.id == id }
            ?.let { included ->
                LibraryEntity(
                    included.id,
                    item.data.id,
                    included.type,
                    included.attributes.subtype,
                    included.attributes.slug,
                    included.attributes.synopsis,
                    included.attributes.canonicalTitle,
                    included.attributes.status,
                    item.data.attributes.status,
                    item.data.attributes.progress,
                    included.attributes.episodeCount ?: included.attributes.chapterCount ?: 0,
                    included.attributes.posterImage ?: ImageModel.empty,
                    included.attributes.coverImage ?: ImageModel.empty,
                    included.attributes.nsfw,
                    included.attributes.startDate ?: "",
                    included.attributes.endDate ?: ""
                )
            }
            ?: error("")
    }
}
