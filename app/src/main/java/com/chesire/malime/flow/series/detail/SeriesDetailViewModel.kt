package com.chesire.malime.flow.series.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chesire.malime.core.models.SeriesModel
import javax.inject.Inject

/**
 * ViewModel to store the current series detail model, and allow interactions with its data.
 */
class SeriesDetailViewModel @Inject constructor() : ViewModel() {
    private val _model = MutableLiveData<SeriesModel>()
    val model: LiveData<SeriesModel> = _model

    /**
     * Updates the currently stored model in the view model.
     */
    fun updateModel(newModel: SeriesModel) = _model.postValue(newModel)
}
