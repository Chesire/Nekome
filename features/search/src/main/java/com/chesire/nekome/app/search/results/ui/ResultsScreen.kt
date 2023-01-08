package com.chesire.nekome.app.search.results.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.InsertPhoto
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.chesire.nekome.app.search.R
import com.chesire.nekome.core.compose.theme.NekomeTheme
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.models.ImageModel

@Composable
fun ResultsScreen(
    viewModel: ResultsViewModel = viewModel()
) {
    val state = viewModel.uiState.collectAsState()
    Render(
        state = state,
        onSeriesTrack = { viewModel.trackNewSeries(it.id, it.type) }
    )
}

@Composable
private fun Render(
    state: State<UIState>,
    onSeriesTrack: (ResultModel) -> Unit
) {
    Scaffold { paddingValues ->
        ResultsList(
            resultModels = state.value.models,
            modifier = Modifier.padding(paddingValues),
            onSeriesTrack = onSeriesTrack
        )
    }
}

@Composable
private fun ResultsList(
    resultModels: List<ResultModel>,
    modifier: Modifier = Modifier,
    onSeriesTrack: (ResultModel) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = resultModels,
            key = { it.id }
        ) { model ->
            ResultItem(model = model, onSeriesTrack = onSeriesTrack)
        }
    }
}

@Composable
private fun ResultItem(model: ResultModel, onSeriesTrack: (ResultModel) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(120.dp)
            .alpha(if (model.canTrack) 1.0f else 0.3f)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = model.posterImage.smallest?.url,
                placeholder = rememberVectorPainter(image = Icons.Default.InsertPhoto),
                error = rememberVectorPainter(image = Icons.Default.InsertPhoto),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(0.7f)
            )
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxHeight()
            ) {
                Text(
                    text = model.canonicalTitle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = model.synopsis,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = model.subtype,
                        style = MaterialTheme.typography.caption
                    )
                    if (model.canTrack) {
                        TextButton(onClick = { onSeriesTrack(model) }) {
                            Text(text = stringResource(id = R.string.results_track))
                        }
                    }
                }
                if (model.isTracking) {
                    LinearProgressIndicator()
                }
            }
        }
    }
}

@Composable
@Preview
private fun Preview() {
    val initialState = UIState(
        models = listOf(
            ResultModel(
                id = 0,
                type = SeriesType.Anime,
                synopsis = "This is a synopsis",
                canonicalTitle = "This is the title",
                subtype = "Oneshot",
                posterImage = ImageModel(
                    tiny = ImageModel.ImageData(
                        url = "",
                        width = 0,
                        height = 0
                    ),
                    small = ImageModel.ImageData(
                        url = "",
                        width = 0,
                        height = 0
                    ),
                    medium = ImageModel.ImageData(
                        url = "",
                        width = 0,
                        height = 0
                    ),
                    large = ImageModel.ImageData(
                        url = "",
                        width = 0,
                        height = 0
                    )
                ),
                canTrack = true,
                isTracking = false
            ),
            ResultModel(
                id = 1,
                type = SeriesType.Anime,
                synopsis = "This is another synopsis",
                canonicalTitle = "This is the title again",
                subtype = "Oneshot",
                posterImage = ImageModel(
                    tiny = ImageModel.ImageData(
                        url = "",
                        width = 0,
                        height = 0
                    ),
                    small = ImageModel.ImageData(
                        url = "",
                        width = 0,
                        height = 0
                    ),
                    medium = ImageModel.ImageData(
                        url = "",
                        width = 0,
                        height = 0
                    ),
                    large = ImageModel.ImageData(
                        url = "",
                        width = 0,
                        height = 0
                    )
                ),
                canTrack = false,
                isTracking = true
            )
        )
    )
    NekomeTheme(darkTheme = true) {
        Render(
            state = produceState(
                initialValue = initialState,
                producer = { value = initialState }
            ),
            onSeriesTrack = { /**/ }
        )
    }
}
