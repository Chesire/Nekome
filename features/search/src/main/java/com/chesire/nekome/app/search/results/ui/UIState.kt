package com.chesire.nekome.app.search.results.ui

import androidx.annotation.StringRes
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.models.ImageModel

data class UIState(
    val models: List<ResultModel>,
    val errorSnackbar: SnackbarData?
)

data class ResultModel(
    val id: Int,
    val type: SeriesType,
    val synopsis: String,
    val canonicalTitle: String,
    val subtype: String,
    val posterImage: ImageModel,
    val canTrack: Boolean,
    val isTracking: Boolean
)

data class SnackbarData(
    @StringRes val stringRes: Int,
    val formatText: String
)
