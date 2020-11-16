package com.chesire.nekome.kitsu.trending

import com.chesire.nekome.trending.api.TrendingEntity
import com.chesire.nekome.trending.api.TrendingEntityMapper

class KitsuTrendingEntityMapper : TrendingEntityMapper<KitsuTrendingEntity> {

    override fun mapFromTrendingEntity(entity: TrendingEntity): KitsuTrendingEntity {
        return KitsuTrendingEntity(
            entity.id,
            entity.type,
            KitsuTrendingEntity.EntityAttributes(
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

    override fun mapToTrendingEntity(item: KitsuTrendingEntity): TrendingEntity {
        return TrendingEntity(
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
