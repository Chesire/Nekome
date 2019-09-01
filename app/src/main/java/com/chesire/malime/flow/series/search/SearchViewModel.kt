package com.chesire.malime.flow.series.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.malime.AuthCaster
import com.chesire.malime.server.Resource
import com.chesire.malime.server.api.SearchApi
import com.chesire.malime.core.flags.SeriesType
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.core.extensions.postError
import com.chesire.malime.core.extensions.postLoading
import com.chesire.malime.core.extensions.postSuccess
import com.chesire.malime.core.flags.AsyncState
import com.chesire.malime.series.SeriesRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val repo: SeriesRepository,
    private val search: SearchApi,
    private val authCaster: AuthCaster
) : ViewModel() {
    private val _searchResults = MutableLiveData<AsyncState<List<SeriesModel>, SearchError>>()

    val searchTitle = MutableLiveData<String>()
    val series: LiveData<List<SeriesModel>>
        get() = repo.series
    val searchResults: LiveData<AsyncState<List<SeriesModel>, SearchError>>
        get() = _searchResults

    var seriesType: SeriesType = SeriesType.Anime

    fun performSearch() = viewModelScope.launch {
        val title = searchTitle.value
        if (title.isNullOrEmpty()) {
            _searchResults.postError(SearchError.MissingTitle)
            return@launch
        }

        _searchResults.postLoading()

        when (val result = when (seriesType) {
            SeriesType.Anime -> search.searchForAnime(title)
            SeriesType.Manga -> search.searchForManga(title)
            else -> error("Unexpected series type provided")
        }) {
            is Resource.Success -> _searchResults.postSuccess(result.data)
            is Resource.Error -> _searchResults.postError(SearchError.Error)
        }
    }

    fun addSeries(model: SeriesModel, startingStatus: UserSeriesStatus) =
        viewModelScope.launch {
            val response = when (model.type) {
                SeriesType.Anime -> repo.addAnime(model.id, startingStatus)
                SeriesType.Manga -> repo.addManga(model.id, startingStatus)
                else -> error("Unexpected series type provided")
            }
            when (response) {
                is Resource.Success -> {
                    // Notify back to UI
                }
                is Resource.Error -> if (response.code == Resource.Error.CouldNotRefresh) {
                    authCaster.issueRefreshingToken()
                }
            }
        }
}
