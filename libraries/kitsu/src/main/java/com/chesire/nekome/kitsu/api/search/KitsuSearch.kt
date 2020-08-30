package com.chesire.nekome.kitsu.api.search

import com.chesire.nekome.core.models.SeriesModel
import com.chesire.nekome.kitsu.parse
import com.chesire.nekome.server.Resource
import com.chesire.nekome.server.api.SearchApi
import javax.inject.Inject

/**
 * Provides an implementation of [SearchApi] to interact with [KitsuSearch] to find new series to
 * track.
 */
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
