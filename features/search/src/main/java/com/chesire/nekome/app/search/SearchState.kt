package com.chesire.nekome.app.search

import com.chesire.nekome.app.search.domain.SearchModel

/**
 * Different states that can occur from searching for a series.
 */
sealed class SearchState {

    /**
     * Async request is currently loading.
     */
    object Loading : SearchState()

    /**
     * Search request for [searchTerm] was successful, and responded with [data].
     */
    data class Success(val searchTerm: String, val data: List<SearchModel>) : SearchState()

    /**
     * No title was provided to search for.
     */
    object EmptyTitle : SearchState()

    /**
     * No type was selected to search for.
     */
    object NoTypeSelected : SearchState()

    /**
     * No series were found for the input search term.
     */
    object NoSeriesFound : SearchState()

    /**
     * An error has occurred with the search request.
     */
    object GenericError : SearchState()
}
