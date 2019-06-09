package com.chesire.malime.flow.series.list.anime

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.malime.AuthCaster
import com.chesire.malime.core.Resource
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.repo.SeriesRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class AnimeViewModel @Inject constructor(
    private val repo: SeriesRepository,
    private val authCaster: AuthCaster
) : ViewModel() {
    val animeSeries = repo.anime

    fun updateSeries(userSeriesId: Int, newProgress: Int, newUserSeriesStatus: UserSeriesStatus) =
        viewModelScope.launch {
            val response = repo.updateSeries(userSeriesId, newProgress, newUserSeriesStatus)
            if (response is Resource.Error && response.code == Resource.Error.CouldNotRefresh) {
                authCaster.issueRefreshingToken()
            }
        }
}
