package com.chesire.nekome.app.search

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekome.core.AuthCaster
import com.chesire.nekome.core.extensions.postError
import com.chesire.nekome.core.extensions.postLoading
import com.chesire.nekome.core.extensions.postSuccess
import com.chesire.nekome.core.flags.AsyncState
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.models.SeriesModel
import com.chesire.nekome.server.Resource
import com.chesire.nekome.server.api.SearchApi
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.launch

/**
 * ViewModel to aid with searching for new series for a user to follow.
 */
class SearchViewModel @ViewModelInject constructor(
    private val searchApi: SearchApi,
    private val authCaster: AuthCaster
) : ViewModel() {

    private val _searchResult = LiveEvent<AsyncState<List<SeriesModel>, SearchError>>()
    val searchResult: LiveData<AsyncState<List<SeriesModel>, SearchError>> = _searchResult

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

    private fun parseSearchResponse(response: Resource<List<SeriesModel>>) = when (response) {
        is Resource.Success ->
            if (response.data.isEmpty()) {
                _searchResult.postError(SearchError.NoSeriesFound)
            } else {
                _searchResult.postSuccess(response.data)
            }
        is Resource.Error ->
            if (response.code == Resource.Error.CouldNotRefresh) {
                authCaster.issueRefreshingToken()
            } else {
                _searchResult.postError(SearchError.GenericError)
            }
    }
}
