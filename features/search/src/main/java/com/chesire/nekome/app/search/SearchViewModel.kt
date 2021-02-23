package com.chesire.nekome.app.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekome.app.search.domain.SearchDomainMapper
import com.chesire.nekome.app.search.domain.SearchModel
import com.chesire.nekome.core.Resource
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

    private val _searchResult = LiveEvent<SearchState>()
    val searchState: LiveData<SearchState> = _searchResult

    /**
     * Executes a search request using the data stored in [model], the result is posted to
     * [searchState].
     */
    fun executeSearch(model: SearchData) {
        if (model.title.isEmpty()) {
            _searchResult.postValue(SearchState.EmptyTitle)
            return
        }
        if (model.seriesType == SeriesType.Unknown) {
            _searchResult.postValue(SearchState.NoTypeSelected)
            return
        }

        _searchResult.postValue(SearchState.Loading)

        viewModelScope.launch {
            val response = when (model.seriesType) {
                SeriesType.Anime -> searchApi.searchForAnime(model.title)
                SeriesType.Manga -> searchApi.searchForManga(model.title)
                else -> error("Unexpected series type provided")
            }

            parseSearchResponse(model, response)
        }
    }

    private fun parseSearchResponse(
        model: SearchData,
        response: Resource<List<SearchDomain>>
    ) = when (response) {
        is Resource.Success ->
            if (response.data.isEmpty()) {
                _searchResult.postValue(SearchState.NoSeriesFound)
            } else {
                _searchResult.postValue(
                    SearchState.Success(
                        model.title,
                        response.data.map { mapper.toSearchModel(it) }
                    )
                )
            }
        is Resource.Error -> _searchResult.postValue(SearchState.GenericError)
    }
}

/**
 * Different states that can occur from searching for a series.
 */
sealed class SearchState {
    object Loading : SearchState()
    data class Success(val searchTerm: String, val data: List<SearchModel>) : SearchState()
    object EmptyTitle : SearchState()
    object NoTypeSelected : SearchState()
    object NoSeriesFound : SearchState()
    object GenericError : SearchState()
}
