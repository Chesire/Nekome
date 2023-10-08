package com.chesire.nekome.feature.serieswidget

import com.chesire.nekome.feature.serieswidget.ui.SeriesWidgetViewModel
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface SeriesWidgetEntryPoint {

    fun seriesWidgetViewModel(): SeriesWidgetViewModel
}
