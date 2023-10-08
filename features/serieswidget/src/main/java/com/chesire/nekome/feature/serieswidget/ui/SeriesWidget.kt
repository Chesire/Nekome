package com.chesire.nekome.feature.serieswidget.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.background
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.color.ColorProvider
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.chesire.nekome.core.compose.theme.DarkColorPalette
import com.chesire.nekome.core.compose.theme.LightColorPalette
import com.chesire.nekome.feature.serieswidget.SeriesWidgetEntryPoint
import dagger.hilt.EntryPoints

class SeriesWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val viewModel = EntryPoints.get(
            context,
            SeriesWidgetEntryPoint::class.java
        ).seriesWidgetViewModel()

        provideContent {
            val state by viewModel.uiState.collectAsState()
            Render(
                state = state,
                updateSeries = { viewModel.execute(ViewAction.UpdateSeries(it)) }
            )
        }
    }

    @Composable
    private fun Render(
        state: UIState,
        updateSeries: (Int) -> Unit
    ) {
        LazyColumn(
            modifier = GlanceModifier
                .appWidgetBackground()
                .background(
                    day = LightColorPalette.surface,
                    night = DarkColorPalette.surface
                )
                .fillMaxSize()
                .padding(8.dp)
        ) {
            items(
                items = state.series,
                itemId = { it.userId.toLong() }
            ) { item ->
                Column {
                    SeriesCard(
                        id = item.userId,
                        title = item.title,
                        progress = item.progress,
                        updateSeries = updateSeries
                    )
                    Spacer(modifier = GlanceModifier.height(8.dp).fillMaxWidth())
                }
            }
        }
    }

    @Composable
    private fun SeriesCard(
        id: Int,
        title: String,
        progress: String,
        updateSeries: (Int) -> Unit,
        modifier: GlanceModifier = GlanceModifier
    ) {
        Row(
            modifier = modifier.fillMaxWidth()
                .background(
                    day = LightColorPalette.primaryContainer,
                    night = DarkColorPalette.primaryContainer
                )
                .cornerRadius(8.dp)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = GlanceModifier
                    .defaultWeight()
                    .padding(end = 8.dp)
            ) {
                Text(
                    text = title,
                    style = TextStyle(
                        color = ColorProvider(
                            day = LightColorPalette.onPrimaryContainer,
                            night = DarkColorPalette.onPrimaryContainer
                        )
                    ),
                    maxLines = 3
                )
                Text(
                    text = progress,
                    style = TextStyle(
                        color = ColorProvider(
                            day = LightColorPalette.onPrimaryContainer,
                            night = DarkColorPalette.onPrimaryContainer
                        )
                    )
                )
            }

            Button(
                text = "+1",
                onClick = { updateSeries(id) }
            )
        }
    }
}
