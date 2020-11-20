package com.chesire.nekome.search.api

import com.chesire.nekome.core.Resource

interface SearchApi {
    /**
     * Search for the anime series [title].
     */
    suspend fun searchForAnime(title: String): Resource<List<SearchEntity>>

    /**
     * Search for the manga series [title].
     */
    suspend fun searchForManga(title: String): Resource<List<SearchEntity>>
}
