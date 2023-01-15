package com.chesire.nekome.app.series.collection.ui

import androidx.annotation.StringRes
import com.chesire.nekome.datasource.series.SeriesDomain

data class UIState(
    val models: List<SeriesDomain>,
    val isRefreshing: Boolean,
    val errorSnackbar: SnackbarData?
)

data class SnackbarData(
    @StringRes val stringRes: Int,
    val formatText: Any = ""
)
