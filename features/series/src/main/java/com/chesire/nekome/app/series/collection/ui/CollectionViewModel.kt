package com.chesire.nekome.app.series.collection.ui

import androidx.lifecycle.ViewModel
import com.chesire.nekome.app.series.collection.core.CollectSeriesUseCase
import com.chesire.nekome.app.series.collection.core.DeleteSeriesUseCase
import com.chesire.nekome.app.series.collection.core.GetFilterPreferenceUseCase
import com.chesire.nekome.app.series.collection.core.GetSortPreferenceUseCase
import com.chesire.nekome.app.series.collection.core.RefreshSeriesUseCase
import com.chesire.nekome.app.series.collection.core.UpdateSeriesUseCase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CollectionViewModel @Inject constructor(
    private val collectSeries: CollectSeriesUseCase,
    private val deleteSeries: DeleteSeriesUseCase,
    private val refreshSeries: RefreshSeriesUseCase,
    private val updateSeries: UpdateSeriesUseCase,
    private val getFilter: GetFilterPreferenceUseCase,
    private val getSort: GetSortPreferenceUseCase
) : ViewModel() {

    // convert the series retrieved from collectSeries using the filter and sort options
}
