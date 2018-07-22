package com.chesire.malime.view.maldisplay

import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.MalimeModel

interface ModelInteractionListener {
    fun showSeriesProfile(model: MalimeModel)
    fun updateSeries(
        model: MalimeModel,
        newProgress: Int,
        newStatus: UserSeriesStatus,
        callback: (success: Boolean) -> Unit
    )
}