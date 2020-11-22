package com.chesire.nekome.kitsu.trending

import com.chesire.nekome.core.EntityMapper
import com.chesire.nekome.trending.api.TrendingEntity
import javax.inject.Inject

/**
 * Provides ability to map instances of [KitsuTrendingEntity] into [TrendingEntity].
 */
class KitsuTrendingEntityMapper @Inject constructor() :
    EntityMapper<KitsuTrendingEntity, TrendingEntity> {

    override fun from(input: KitsuTrendingEntity) = TrendingEntity(
        input.id,
        input.type,
        input.attributes.slug,
        input.attributes.synopsis,
        input.attributes.canonicalTitle,
        "",
        input.attributes.startDate,
        input.attributes.endDate,
        input.attributes.subtype,
        input.attributes.status,
        input.attributes.posterImage,
        input.attributes.coverImage,
        input.attributes.chapterCount,
        input.attributes.episodeCount,
        input.attributes.nsfw
    )
}
