package com.chesire.nekome.app.search.results.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
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
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(resultModels) { model ->
            Card(
                modifier = Modifier.alpha(if (model.canTrack) 1.0f else 0.3f)
            ) {
                Text(text = model.canonicalTitle)
                Text(text = model.synopsis)
                Text(text = model.subtype)
                AsyncImage(
                    model = model.posterImage.smallest?.url,
                    placeholder = painterResource(id = R.drawable.ic_insert_photo),
                    error = painterResource(id = R.drawable.ic_insert_photo),
                    contentDescription = null
                )
                if (model.canTrack) {
                    IconButton(onClick = { onSeriesTrack(model) }) {

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
                subtype = "This is a subtype",
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
                isTracking = false
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
