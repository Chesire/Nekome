package com.chesire.nekome.kitsu.search

import com.chesire.nekome.search.api.SearchEntity
import com.chesire.nekome.search.api.SearchEntityMapper
import javax.inject.Inject

class KitsuSearchEntityMapper @Inject constructor() : SearchEntityMapper<KitsuSearchEntity> {
    override fun mapFromSearchEntity(entity: SearchEntity): KitsuSearchEntity {
        TODO("Not yet implemented")
    }

    override fun mapToSearchEntity(item: KitsuSearchEntity): SearchEntity {
        TODO("Not yet implemented")
    }
}
