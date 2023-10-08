package com.chesire.nekome.feature.serieswidget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.Text

class SeriesWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            Render()
        }
    }

    @Composable
    private fun Render() {
        Column(modifier = GlanceModifier.fillMaxSize()) {
            Text(text = "Test")
        }
    }
}
