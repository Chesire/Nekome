package com.chesire.nekome.datasource.search.remote

import com.chesire.nekome.core.models.ErrorDomain
import com.chesire.nekome.datasource.search.SearchDomain
import com.github.michaelbull.result.Result

/**
 * Methods relating to searching for series.
 */
interface SearchApi {

    /**
     * Search for the anime series [title].
     */
    suspend fun searchForAnime(title: String): Result<List<SearchDomain>, ErrorDomain>

    /**
     * Search for the manga series [title].
     */
    suspend fun searchForManga(title: String): Result<List<SearchDomain>, ErrorDomain>
}
