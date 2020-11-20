package com.chesire.nekome.kitsu.search

import com.chesire.nekome.search.api.SearchEntity
import com.chesire.nekome.search.api.SearchEntityMapper
import javax.inject.Inject

class KitsuSearchEntityMapper @Inject constructor() : SearchEntityMapper<KitsuSearchEntity> {

    override fun mapFromSearchEntity(entity: SearchEntity): KitsuSearchEntity {
        return KitsuSearchEntity(
            entity.id,
            entity.type,
            KitsuSearchEntity.EntityAttributes(
                entity.slug,
                entity.synopsis,
                entity.canonicalTitle,
                entity.startDate,
                entity.endDate,
                entity.subtype,
                entity.status,
                entity.posterImage,
                entity.coverImage,
                entity.chapterCount,
                entity.episodeCount,
                entity.nsfw
            )
        )
    }

    override fun mapToSearchEntity(item: KitsuSearchEntity): SearchEntity {
        return SearchEntity(
            item.id,
            item.type,
            item.attributes.slug,
            item.attributes.synopsis,
            item.attributes.canonicalTitle,
            "",
            item.attributes.startDate,
            item.attributes.endDate,
            item.attributes.subtype,
            item.attributes.status,
            item.attributes.posterImage,
            item.attributes.coverImage,
            item.attributes.chapterCount,
            item.attributes.episodeCount,
            item.attributes.nsfw
        )
    }
}
