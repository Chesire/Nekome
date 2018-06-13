package com.chesire.malime.view.search

import com.chesire.malime.core.models.MalimeModel

interface Search2InteractionListener {
    fun addNewSeries(selectedSeries: MalimeModel, callback: (Boolean) -> Unit)
    fun navigateToSeries(selectedSeries: MalimeModel)
}