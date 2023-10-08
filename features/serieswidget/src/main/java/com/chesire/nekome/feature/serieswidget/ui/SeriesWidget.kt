package com.chesire.nekome.feature.serieswidget.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.text.Text
import com.chesire.nekome.feature.serieswidget.SeriesWidgetEntryPoint
import dagger.hilt.EntryPoints

class SeriesWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {

        provideContent {
            ProvideContent()
        }
    }

    @Composable
    private fun ProvideContent() {
        val ctx = LocalContext.current.applicationContext

        // TODO: Use remember for this?
        val viewModel = EntryPoints.get(
            ctx,
            SeriesWidgetEntryPoint::class.java
        ).seriesWidgetViewModel()

        val state by viewModel.uiState.collectAsState()
        Render(
            state = state,
            updateSeries = { viewModel.execute(ViewAction.UpdateSeries(it)) }
        )
    }

    @Composable
    private fun Render(
        state: UIState,
        updateSeries: (Int) -> Unit
    ) {
        LazyColumn(
            modifier = GlanceModifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            items(
                items = state.series,
                itemId = { it.userId.toLong() }
            ) { item ->
                SeriesCard(
                    id = item.userId,
                    title = item.title,
                    progress = item.progress,
                    isUpdating = item.isUpdating,
                    updateSeries = updateSeries
                )
            }
        }
    }

    @Composable
    private fun SeriesCard(
        id: Int,
        title: String,
        progress: String,
        isUpdating: Boolean,
        updateSeries: (Int) -> Unit
    ) {
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = title)
                Text(text = progress)
            }
            Spacer(modifier = GlanceModifier.defaultWeight())
            Button(text = "+1", onClick = { updateSeries(id) })
        }
    }
}
