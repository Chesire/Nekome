package com.chesire.nekome.server.api

import com.chesire.nekome.core.models.SeriesModel
import com.chesire.nekome.core.Resource

/**
 * Methods relating to search.
 */
interface SearchApi {
    /**
     * Search for the anime series [title].
     */
    suspend fun searchForAnime(title: String): Resource<List<SeriesModel>>

    /**
     * Search for the manga series [title].
     */
    suspend fun searchForManga(title: String): Resource<List<SeriesModel>>
}
