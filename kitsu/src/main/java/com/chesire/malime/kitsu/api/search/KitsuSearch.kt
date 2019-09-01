package com.chesire.malime.kitsu.api.search

import com.chesire.malime.server.Resource
import com.chesire.malime.server.api.SearchApi
import com.chesire.malime.server.models.SeriesModel
import com.chesire.malime.kitsu.parse
import javax.inject.Inject

@Suppress("TooGenericExceptionCaught")
class KitsuSearch @Inject constructor(private val searchService: KitsuSearchService) : SearchApi {
    override suspend fun searchForAnime(title: String): Resource<List<SeriesModel>> {
        return try {
            searchService.searchForAnimeAsync(title).parse()
        } catch (ex: Exception) {
            ex.parse()
        }
    }

    override suspend fun searchForManga(title: String): Resource<List<SeriesModel>> {
        return try {
            searchService.searchForMangaAsync(title).parse()
        } catch (ex: Exception) {
            ex.parse()
        }
    }
}
