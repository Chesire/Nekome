package com.chesire.malime.flow.series.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chesire.malime.core.models.SeriesModel
import javax.inject.Inject

class SeriesDetailViewModel @Inject constructor() : ViewModel() {
    private val _model = MutableLiveData<SeriesModel>()
    val model: LiveData<SeriesModel> = _model

    fun updateModel(newModel: SeriesModel) {
        _model.postValue(newModel)
    }
}
