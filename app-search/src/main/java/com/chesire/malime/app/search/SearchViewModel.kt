package com.chesire.malime.app.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.malime.core.extensions.postError
import com.chesire.malime.core.extensions.postSuccess
import com.chesire.malime.core.flags.AsyncState
import com.chesire.malime.core.flags.SeriesType
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.server.Resource
import com.chesire.malime.server.api.SearchApi
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel to aid with searching for new series for a user to follow.
 */
class SearchViewModel @Inject constructor(private val searchApi: SearchApi) : ViewModel() {
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

        viewModelScope.launch {
            when (val result = when (model.seriesType) {
                SeriesType.Anime -> searchApi.searchForAnime(model.title)
                SeriesType.Manga -> searchApi.searchForManga(model.title)
                else -> error("Unexpected series type provided")
            }) {
                is Resource.Success ->
                    if (result.data.isEmpty()) {
                        _searchResult.postError(SearchError.NoSeriesFound)
                    } else {
                        _searchResult.postSuccess(result.data)
                    }
                is Resource.Error -> _searchResult.postError(SearchError.GenericError)
            }
        }
    }
}
