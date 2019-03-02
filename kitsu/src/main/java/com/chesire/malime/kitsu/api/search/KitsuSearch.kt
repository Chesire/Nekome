package com.chesire.malime.kitsu.api.search

import com.chesire.malime.core.api.SearchApi
import com.chesire.malime.kitsu.parse
import javax.inject.Inject

class KitsuSearch @Inject constructor(private val searchService: KitsuSearchService) : SearchApi {
    override suspend fun searchForAnime(title: String) =
        searchService.searchForAnimeAsync(title).await().parse()

    override suspend fun searchForManga(title: String) =
        searchService.searchForMangaAsync(title).await().parse()
}
