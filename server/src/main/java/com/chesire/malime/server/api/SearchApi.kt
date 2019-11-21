package com.chesire.malime.server.api

import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.server.Resource

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
