package com.chesire.nekome.app.discover

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekome.app.discover.domain.DiscoverDomainMapper
import com.chesire.nekome.app.discover.domain.TrendingModel
import com.chesire.nekome.core.Resource
import com.chesire.nekome.core.extensions.postError
import com.chesire.nekome.core.extensions.postSuccess
import com.chesire.nekome.core.flags.AsyncState
import com.chesire.nekome.trending.api.TrendingApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel to aid with performing series discovery.
 */
@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val trending: TrendingApi,
    private val mapper: DiscoverDomainMapper
) : ViewModel() {

    private val _trendingAnime by lazy {
        val trendingData = MutableLiveData<AsyncState<List<TrendingModel>, DiscoverError>>(
            AsyncState.Loading()
        )
        viewModelScope.launch {
            when (val animeList = trending.getTrendingAnime()) {
                is Resource.Success -> trendingData.postSuccess(
                    animeList.data.map { mapper.toTrendingModel(it) }
                )
                is Resource.Error -> trendingData.postError(DiscoverError.Error)
            }
        }
        return@lazy trendingData
    }

    private val _trendingManga by lazy {
        val trendingData = MutableLiveData<AsyncState<List<TrendingModel>, DiscoverError>>(
            AsyncState.Loading()
        )
        viewModelScope.launch {
            when (val mangaList = trending.getTrendingManga()) {
                is Resource.Success -> trendingData.postSuccess(
                    mangaList.data.map { mapper.toTrendingModel(it) }
                )
                is Resource.Error -> trendingData.postError(DiscoverError.Error)
            }
        }
        return@lazy trendingData
    }

    /**
     * Get the current trending Anime series.
     */
    val trendingAnime: LiveData<AsyncState<List<TrendingModel>, DiscoverError>>
        get() = _trendingAnime

    /**
     * Get the current trending Manga series.
     */
    val trendingManga: LiveData<AsyncState<List<TrendingModel>, DiscoverError>>
        get() = _trendingManga
}
