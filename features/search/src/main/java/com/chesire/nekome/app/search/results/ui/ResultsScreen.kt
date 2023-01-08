package com.chesire.nekome.app.search.results.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.InsertPhoto
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
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
        onSeriesTrack = { viewModel.trackNewSeries(it) },
        onSnackbarShown = { viewModel.errorSnackbarObserved() }
    )
}

@Composable
private fun Render(
    state: State<UIState>,
    onSeriesTrack: (ResultModel) -> Unit,
    onSnackbarShown: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.semantics { testTag = ResultsTags.Snackbar }
            )
        },
        modifier = Modifier.semantics { testTag = ResultsTags.Root }
    ) { paddingValues ->
        ResultsList(
            resultModels = state.value.models,
            modifier = Modifier.padding(paddingValues),
            onSeriesTrack = onSeriesTrack
        )
    }

    val snackbar = state.value.errorSnackbar
    if (snackbar != null) {
        val message = stringResource(id = snackbar.stringRes, snackbar.formatText)
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message = message, duration = SnackbarDuration.Short)
            onSnackbarShown()
        }
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
            .height(120.dp)
            .alpha(if (model.canTrack) 1.0f else 0.3f)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (model.isTracking) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                )
            }
            Row(modifier = Modifier.fillMaxSize()) {
                AsyncImage(
                    model = model.posterImage.smallest?.url,
                    placeholder = rememberVectorPainter(image = Icons.Default.InsertPhoto),
                    error = rememberVectorPainter(image = Icons.Default.BrokenImage),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(0.25f)
                        .aspectRatio(0.7f)
                        .align(Alignment.CenterVertically)
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
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = model.subtype,
                        style = MaterialTheme.typography.caption
                    )
                }
            }
            if (model.canTrack && !model.isTracking) {
                // TODO: Make this a bit more pronouncd as a button
                IconButton(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    onClick = { onSeriesTrack(model) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.results_track_series)
                    )
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
        ),
        errorSnackbar = null
    )
    NekomeTheme(darkTheme = true) {
        Render(
            state = produceState(
                initialValue = initialState,
                producer = { value = initialState }
            ),
            onSeriesTrack = { /**/ },
            onSnackbarShown = { /**/ }
        )
    }
}


object ResultsTags {
    const val Root = "ResultsRoot"
    const val Snackbar = "ResultsSnackbar"
}
