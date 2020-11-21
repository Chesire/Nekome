package com.chesire.nekome.kitsu.search

import com.chesire.nekome.core.EntityMapper
import com.chesire.nekome.search.api.SearchEntity
import javax.inject.Inject

class KitsuSearchEntityMapper @Inject constructor() :
    EntityMapper<KitsuSearchEntity, SearchEntity> {

    override fun from(input: KitsuSearchEntity) = SearchEntity(
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
