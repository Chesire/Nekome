package com.chesire.malime.flow.series.detail

import androidx.lifecycle.ViewModel
import com.chesire.malime.core.models.SeriesModel
import javax.inject.Inject

class SeriesDetailViewModel @Inject constructor() : ViewModel() {
    lateinit var model: SeriesModel

    fun deleteSeries() {
        // execute delete series
    }
}
