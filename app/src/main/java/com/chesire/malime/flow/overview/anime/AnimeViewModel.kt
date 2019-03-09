package com.chesire.malime.flow.overview.anime

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.repo.SeriesRepository
import javax.inject.Inject

class AnimeViewModel @Inject constructor(
    private val repo: SeriesRepository
) : ViewModel() {
    val animeSeries: LiveData<List<SeriesModel>>
        get() = repo.anime

}
