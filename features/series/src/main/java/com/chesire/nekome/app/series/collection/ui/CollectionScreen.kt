@file:OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialApi::class)

package com.chesire.nekome.app.series.collection.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chesire.nekome.app.series.R
import com.chesire.nekome.core.compose.theme.NekomeTheme
import com.chesire.nekome.core.flags.SeriesStatus
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.datasource.series.SeriesDomain

@Composable
fun CollectionScreen(
    viewModel: CollectionViewModel = viewModel()
) {
    val state = viewModel.uiState.collectAsState()
    Render(
        state = state,
        onRefresh = { viewModel.execute(ViewAction.PerformSeriesRefresh) },
        onSeriesSelect = { viewModel /* Add method to call */ },
        onIncrementSeries = { viewModel.execute(ViewAction.IncrementSeriesPressed(it)) },
        onSnackbarShown = { viewModel.execute(ViewAction.ErrorSnackbarObserved) }
    )
}

@Composable
private fun Render(
    state: State<UIState>,
    onRefresh: () -> Unit,
    onSeriesSelect: (SeriesDomain) -> Unit,
    onIncrementSeries: (SeriesDomain) -> Unit,
    onSnackbarShown: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        }
    ) { paddingValues ->
        SeriesCollection(
            models = state.value.models,
            isRefreshing = state.value.isRefreshing,
            modifier = Modifier.padding(paddingValues),
            onRefresh = onRefresh,
            onSeriesSelect = onSeriesSelect,
            onIncrementSeries = onIncrementSeries
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
private fun SeriesCollection(
    models: List<SeriesDomain>,
    isRefreshing: Boolean,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit,
    onSeriesSelect: (SeriesDomain) -> Unit,
    onIncrementSeries: (SeriesDomain) -> Unit
) {
    if (models.isNotEmpty()) {
        val pullRefreshState = rememberPullRefreshState(
            refreshing = isRefreshing,
            onRefresh = onRefresh
        )
        Box(modifier = modifier.pullRefresh(pullRefreshState)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = models,
                    key = { it.id }
                ) {
                    SeriesItem(
                        model = it,
                        onSeriesSelect = onSeriesSelect,
                        onIncrementSeries = onIncrementSeries
                    )
                }
            }
            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = stringResource(id = R.string.series_list_empty),
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun SeriesItem(
    model: SeriesDomain,
    onSeriesSelect: (SeriesDomain) -> Unit,
    onIncrementSeries: (SeriesDomain) -> Unit
) {

}

@Composable
@Preview
private fun Preview() {
    val initialState = UIState(
        models = listOf(
            SeriesDomain(
                id = 0,
                userId = 0,
                type = SeriesType.Anime,
                subtype = Subtype.Movie,
                slug = "slug",
                title = "Title",
                seriesStatus = SeriesStatus.Current,
                userSeriesStatus = UserSeriesStatus.Current,
                progress = 0,
                totalLength = 12,
                rating = 0,
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
                startDate = "",
                endDate = ""
            )
        ),
        isRefreshing = false,
        errorSnackbar = null
    )
    NekomeTheme(darkTheme = true) {
        Render(
            state = produceState(
                initialValue = initialState,
                producer = { value = initialState }
            ),
            onRefresh = { /**/ },
            onSeriesSelect = { /**/ },
            onIncrementSeries = { /**/ },
            onSnackbarShown = { /**/ }
        )
    }
}
