package com.chesire.malime.view.search

import com.chesire.malime.core.models.MalimeModel

interface SearchInteractionListener {
    fun addNewSeries(selectedSeries: MalimeModel, callback: (Boolean) -> Unit)
    fun showSeriesProfile(selectedSeries: MalimeModel)
}