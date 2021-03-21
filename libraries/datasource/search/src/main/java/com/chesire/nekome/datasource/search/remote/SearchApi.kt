package com.chesire.nekome.datasource.search.remote

import com.chesire.nekome.core.Resource
import com.chesire.nekome.datasource.search.SearchDomain

/**
 * Methods relating to searching for series.
 */
interface SearchApi {

    /**
     * Search for the anime series [title].
     */
    suspend fun searchForAnime(title: String): Resource<List<SearchDomain>>

    /**
     * Search for the manga series [title].
     */
    suspend fun searchForManga(title: String): Resource<List<SearchDomain>>
}
