package com.chesire.malime.kitsu.api.search

import com.chesire.malime.core.Resource
import com.chesire.malime.core.api.SearchApi
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.kitsu.parse
import javax.inject.Inject

class KitsuSearch @Inject constructor(private val searchService: KitsuSearchService) : SearchApi {
    override suspend fun searchForAnime(title: String): Resource<List<SeriesModel>> {
        return try {
            searchService.searchForAnimeAsync(title).await().parse()
        } catch (ex: Exception) {
            ex.parse()
        }
    }

    override suspend fun searchForManga(title: String): Resource<List<SeriesModel>> {
        return try {
            searchService.searchForMangaAsync(title).await().parse()
        } catch (ex: Exception) {
            ex.parse()
        }
    }
}
