package com.chesire.malime.flow.series.list.anime

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.malime.AuthCaster
import com.chesire.malime.core.Resource
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.repo.SeriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AnimeViewModel @Inject constructor(private val repo: SeriesRepository) : ViewModel() {
    val animeSeries: LiveData<List<SeriesModel>>
        get() = repo.anime

    fun updateSeries(userSeriesId: Int, newProgress: Int, newUserSeriesStatus: UserSeriesStatus) {
        viewModelScope.launch {
            val response = withContext(Dispatchers.Default) {
                repo.updateSeries(userSeriesId, newProgress, newUserSeriesStatus)
            }
            if (response is Resource.Error && response.code == 401) {
                AuthCaster.issueRefreshingToken()
            }
        }
    }
}
