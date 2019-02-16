package com.chesire.malime.kitsu.api.search

import com.chesire.malime.core.Resource
import com.chesire.malime.core.api.SearchApi
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.kitsu.api.ResponseParser

class KitsuSearch(private val searchService: KitsuSearchService) : ResponseParser, SearchApi {
    override suspend fun searchForAnime(title: String): Resource<List<SeriesModel>> {
        val response = searchService.searchForAnimeAsync(title).await()
        return parseResponse(response)
    }

    override suspend fun searchForManga(title: String): Resource<List<SeriesModel>> {
        val response = searchService.searchForMangaAsync(title).await()
        return parseResponse(response)
    }
}
