package com.chesire.malime.view.maldisplay

import com.chesire.malime.core.models.MalimeModel

interface ModelInteractionListener {
    fun onImageClicked(model: MalimeModel)
    fun onSeriesSetProgress(
        model: MalimeModel,
        newProgress: Int,
        callback: (success: Boolean) -> Unit
    )
}