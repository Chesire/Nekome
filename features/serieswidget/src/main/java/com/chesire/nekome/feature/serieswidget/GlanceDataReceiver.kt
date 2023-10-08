package com.chesire.nekome.feature.serieswidget

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.chesire.nekome.feature.serieswidget.ui.SeriesWidget

class GlanceDataReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = SeriesWidget()
}
