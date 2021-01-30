package com.chesire.nekome.app.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekome.app.search.domain.SearchDomainMapper
import com.chesire.nekome.app.search.domain.SearchModel
import com.chesire.nekome.core.Resource
import com.chesire.nekome.core.extensions.postError
import com.chesire.nekome.core.extensions.postLoading
import com.chesire.nekome.core.extensions.postSuccess
import com.chesire.nekome.core.flags.AsyncState
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.search.api.SearchApi
import com.chesire.nekome.search.api.SearchDomain
import com.hadilq.liveevent.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel to aid with searching for new series for a user to follow.
 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchApi: SearchApi,
    private val mapper: SearchDomainMapper
) : ViewModel() {

    private val _searchResult = LiveEvent<AsyncState<List<SearchModel>, SearchError>>()
    val searchResult: LiveData<AsyncState<List<SearchModel>, SearchError>> = _searchResult

    /**
     * Executes a search request using the data stored in [model], the result is posted to
     * [searchResult].
     */
    fun executeSearch(model: SearchData) {
        if (model.title.isEmpty()) {
            _searchResult.postError(SearchError.EmptyTitle)
            return
        }
        if (model.seriesType == SeriesType.Unknown) {
            _searchResult.postError(SearchError.NoTypeSelected)
            return
        }

        _searchResult.postLoading()

        viewModelScope.launch {
            val response = when (model.seriesType) {
                SeriesType.Anime -> searchApi.searchForAnime(model.title)
                SeriesType.Manga -> searchApi.searchForManga(model.title)
                else -> error("Unexpected series type provided")
            }

            parseSearchResponse(response)
        }
    }

    private fun parseSearchResponse(response: Resource<List<SearchDomain>>) = when (response) {
        is Resource.Success ->
            if (response.data.isEmpty()) {
                _searchResult.postError(SearchError.NoSeriesFound)
            } else {
                _searchResult.postSuccess(response.data.map { mapper.toSearchModel(it) })
            }
        is Resource.Error -> _searchResult.postError(SearchError.GenericError)
    }
}
