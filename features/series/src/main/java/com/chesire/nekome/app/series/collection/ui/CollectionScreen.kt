@file:OptIn(ExperimentalMaterialApi::class)

package com.chesire.nekome.app.series.collection.ui

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
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
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.InsertPhoto
import androidx.compose.material.icons.filled.PlusOne
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.chesire.nekome.app.series.R
import com.chesire.nekome.core.compose.theme.NekomeTheme
import com.chesire.nekome.core.flags.Subtype

@Composable
fun CollectionScreen(
    viewModel: CollectionViewModel = viewModel()
) {
    val state = viewModel.uiState.collectAsState()
    Render(
        state = state,
        onRefresh = { viewModel.execute(ViewAction.PerformSeriesRefresh) },
        onSelectSeries = { viewModel /* Add method to call */ },
        onIncrementSeries = { viewModel.execute(ViewAction.IncrementSeriesPressed(it)) },
        onSnackbarShown = { viewModel.execute(ViewAction.ErrorSnackbarObserved) }
    )
}

@Composable
private fun Render(
    state: State<UIState>,
    onRefresh: () -> Unit,
    onSelectSeries: (Series) -> Unit,
    onIncrementSeries: (Series) -> Unit,
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
            onSelectSeries = onSelectSeries,
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
    models: List<Series>,
    isRefreshing: Boolean,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit,
    onSelectSeries: (Series) -> Unit,
    onIncrementSeries: (Series) -> Unit
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
                    key = { it.userId }
                ) {
                    SeriesItem(
                        model = it,
                        onSelectSeries = onSelectSeries,
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
    model: Series,
    onSelectSeries: (Series) -> Unit,
    onIncrementSeries: (Series) -> Unit
) {
    val context = LocalContext.current
    val dateString = remember {
        buildDateString(
            context = context,
            startDate = model.startDate,
            endDate = model.endDate
        )
    }
    Card(
        onClick = { onSelectSeries(model) },
        modifier = Modifier
            .fillMaxWidth()
            .height(125.dp)

    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (model.isUpdating) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                )
            }
            Row(modifier = Modifier.fillMaxSize()) {
                AsyncImage(
                    model = model.posterImageUrl,
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
                        text = model.title,
                        maxLines = 1, // TODO: Maybe do 2 lines?
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = model.subtype,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.overline,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(text = dateString)
                    Divider()
                    Text(text = model.progress)
                }
            }
            IconButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .alpha(if (model.isUpdating) 0.3f else 1f),
                enabled = !model.isUpdating,
                onClick = { onIncrementSeries(model) }
            ) {
                Icon(
                    imageVector = Icons.Default.PlusOne,
                    contentDescription = stringResource(id = R.string.series_list_plus_one),
                    tint = MaterialTheme.colors.primary
                )
            }
        }
    }
}

private fun buildDateString(context: Context, startDate: String, endDate: String): String {
    return when {
        startDate.isEmpty() && endDate.isEmpty() -> context.getString(R.string.series_list_unknown)
        startDate == endDate -> startDate
        endDate.isEmpty() -> context.getString(
            R.string.series_list_date_range,
            startDate,
            context.getString(R.string.series_list_ongoing)
        )
        else -> context.getString(
            R.string.series_list_date_range,
            startDate,
            endDate
        )
    }
}

@Composable
@Preview
private fun Preview() {
    val initialState = UIState(
        models = listOf(
            Series(
                userId = 0,
                subtype = Subtype.Movie.name,
                title = "Title",
                progress = "0",
                startDate = "Start date",
                endDate = "End date",
                posterImageUrl = "",
                isUpdating = false,
                showPlusOne = true
            ),
            Series(
                userId = 1,
                subtype = Subtype.TV.name,
                title = "Title 2",
                progress = "3 / 5",
                startDate = "Start date",
                endDate = "End date",
                posterImageUrl = "",
                isUpdating = true,
                showPlusOne = true
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
            onSelectSeries = { /**/ },
            onIncrementSeries = { /**/ },
            onSnackbarShown = { /**/ }
        )
    }
}
