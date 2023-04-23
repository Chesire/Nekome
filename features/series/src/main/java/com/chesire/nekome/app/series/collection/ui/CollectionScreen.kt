@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)

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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.InsertPhoto
import androidx.compose.material.icons.filled.PlusOne
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.chesire.nekome.app.series.R
import com.chesire.nekome.core.compose.composables.NekomeDialog
import com.chesire.nekome.core.compose.theme.NekomeTheme
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.preferences.flags.SortOption

@Composable
fun CollectionScreen(
    viewModel: CollectionViewModel = hiltViewModel(),
    navigateToItem: (Int, String) -> Unit
) {
    val state = viewModel.uiState.collectAsState()

    state.value.seriesDetails?.let {
        LaunchedEffect(it.show) {
            navigateToItem(it.seriesId, it.seriesTitle)
            viewModel.execute(ViewAction.SeriesNavigationObserved)
        }
    }
    Render(
        state = state,
        onRefresh = { viewModel.execute(ViewAction.PerformSeriesRefresh) },
        onSelectSeries = { viewModel.execute(ViewAction.SeriesPressed(it)) },
        onIncrementSeries = { viewModel.execute(ViewAction.IncrementSeriesPressed(it)) },
        onRatingComplete = { series, rating ->
            viewModel.execute(ViewAction.IncrementSeriesWithRating(series, rating))
        },
        onSnackbarShown = { viewModel.execute(ViewAction.ErrorSnackbarObserved) },
        onSortPressed = { viewModel.execute(ViewAction.SortPressed) },
        onSortResult = { viewModel.execute(ViewAction.PerformSort(it)) },
        onFilterPressed = { viewModel.execute(ViewAction.FilterPressed) },
        onFilterResult = { viewModel.execute(ViewAction.PerformFilter(it)) }
    )
}

@Composable
private fun Render(
    state: State<UIState>,
    onRefresh: () -> Unit,
    onSelectSeries: (Series) -> Unit,
    onIncrementSeries: (Series) -> Unit,
    onRatingComplete: (Series, Int?) -> Unit,
    onSnackbarShown: () -> Unit,
    onSortPressed: () -> Unit,
    onSortResult: (SortOption?) -> Unit,
    onFilterPressed: () -> Unit,
    onFilterResult: (List<FilterOption>?) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = state.value.screenTitle))
                },
                actions = {
                    IconButton(
                        onClick = onFilterPressed,
                        modifier = Modifier.semantics { testTag = SeriesCollectionTags.MenuFilter }
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterAlt,
                            contentDescription = stringResource(id = R.string.menu_filter)
                        )
                    }
                    IconButton(
                        onClick = onSortPressed,
                        modifier = Modifier.semantics { testTag = SeriesCollectionTags.MenuSort }
                    ) {
                        Icon(
                            imageVector = Icons.Default.SortByAlpha,
                            contentDescription = stringResource(id = R.string.menu_sort)
                        )
                    }
                    IconButton(
                        onClick = onRefresh,
                        modifier = Modifier.semantics { testTag = SeriesCollectionTags.MenuRefresh }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = stringResource(id = R.string.series_list_refresh)
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.semantics { testTag = SeriesCollectionTags.Snackbar }
            )
        },
        modifier = Modifier.semantics { testTag = SeriesCollectionTags.Root }
    ) { paddingValues ->
        if (state.value.isInitializing) {
            CircularProgressIndicator()
        } else {
            SeriesCollection(
                models = state.value.models,
                isRefreshing = state.value.isRefreshing,
                modifier = Modifier.padding(paddingValues),
                onRefresh = onRefresh,
                onSelectSeries = onSelectSeries,
                onIncrementSeries = onIncrementSeries
            )
        }
    }

    SortDialog(
        sortOptions = state.value.sortDialog,
        onSortResult = onSortResult
    )
    RatingDialog(
        ratingDialog = state.value.ratingDialog,
        onRatingComplete = onRatingComplete
    )
    FilterDialog(
        filterDialog = state.value.filterDialog,
        onFilterResult = onFilterResult
    )
    RenderSnackbar(
        snackbarData = state.value.errorSnackbar,
        snackbarHostState = snackbarHostState,
        onSnackbarShown = onSnackbarShown
    )
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
        Box(
            modifier = modifier
                .pullRefresh(pullRefreshState)
                .semantics { testTag = SeriesCollectionTags.RefreshContainer }
        ) {
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .semantics { testTag = SeriesCollectionTags.EmptyView }
        ) {
            Text(
                text = stringResource(id = R.string.series_list_empty),
                style = MaterialTheme.typography.titleMedium,
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
            .semantics { testTag = SeriesCollectionTags.SeriesItem }
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
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp, 0.dp, 0.dp, 8.dp)
                    )
                    Text(
                        text = "${model.subtype}   $dateString",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp, 0.dp, 0.dp, 16.dp)
                    )
                    Divider()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp, 8.dp, 0.dp, 0.dp),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = model.progress,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        if (model.showPlusOne) {
                            IconButton(
                                modifier = Modifier
                                    .alpha(if (model.isUpdating) 0.3f else 1f)
                                    .align(Alignment.CenterVertically)
                                    .semantics { testTag = SeriesCollectionTags.PlusOne },
                                enabled = !model.isUpdating,
                                onClick = { onIncrementSeries(model) }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlusOne,
                                    contentDescription = stringResource(
                                        id = R.string.series_list_plus_one
                                    ),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
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
private fun SortDialog(sortOptions: Sort, onSortResult: (SortOption?) -> Unit) {
    if (sortOptions.show) {
        NekomeDialog(
            title = R.string.sort_dialog_title,
            confirmButton = R.string.ok,
            cancelButton = R.string.cancel,
            currentValue = sortOptions.currentSort,
            allValues = sortOptions
                .sortOptions
                .associateWith { stringResource(id = it.stringId) }
                .toList(),
            onResult = onSortResult
        )
    }
}

@Composable
private fun RenderSnackbar(
    snackbarData: SnackbarData?,
    snackbarHostState: SnackbarHostState,
    onSnackbarShown: () -> Unit
) {
    snackbarData?.let { snackbar ->
        val message = stringResource(id = snackbar.stringRes, snackbar.formatText)
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message = message, duration = SnackbarDuration.Short)
            onSnackbarShown()
        }
    }
}

@Composable
@Preview
private fun Preview() {
    val initialState = UIState(
        screenTitle = R.string.nav_anime,
        isInitializing = true,
        models = listOf(
            Series(
                userId = 0,
                subtype = Subtype.Movie.name,
                title = "Title",
                progress = "0",
                startDate = "2022-10-11",
                endDate = "2022-12-27",
                posterImageUrl = "",
                rating = 1,
                isUpdating = false,
                showPlusOne = true
            ),
            Series(
                userId = 1,
                subtype = Subtype.TV.name,
                title = "Title 2",
                progress = "3 / 5",
                startDate = "2022-10-11",
                endDate = "2022-12-27",
                posterImageUrl = "",
                rating = 2,
                isUpdating = false,
                showPlusOne = true
            )
        ),
        isRefreshing = false,
        ratingDialog = null,
        errorSnackbar = null,
        seriesDetails = null,
        sortDialog = Sort(
            show = false,
            currentSort = SortOption.Title,
            sortOptions = listOf(
                SortOption.Default,
                SortOption.Title,
                SortOption.StartDate,
                SortOption.EndDate,
                SortOption.Rating
            )
        ),
        filterDialog = Filter(
            show = false,
            filterOptions = listOf(
                FilterOption(UserSeriesStatus.Current, true),
                FilterOption(UserSeriesStatus.Completed, true),
                FilterOption(UserSeriesStatus.OnHold, true),
                FilterOption(UserSeriesStatus.Dropped, true),
                FilterOption(UserSeriesStatus.Planned, true)
            )
        )
    )
    NekomeTheme(isDarkTheme = true) {
        Render(
            state = produceState(
                initialValue = initialState,
                producer = { value = initialState }
            ),
            onRefresh = { /**/ },
            onSelectSeries = { /**/ },
            onIncrementSeries = { /**/ },
            onRatingComplete = { _, _ -> /**/ },
            onSnackbarShown = { /**/ },
            onSortPressed = { /**/ },
            onSortResult = { /**/ },
            onFilterPressed = { /**/ },
            onFilterResult = { /**/ }
        )
    }
}

object SeriesCollectionTags {
    const val Root = "SeriesCollectionRoot"
    const val EmptyView = "SeriesCollectionEmptyView"
    const val RefreshContainer = "SeriesCollectionRefreshContainer"
    const val SeriesItem = "SeriesCollectionSeriesItem"
    const val PlusOne = "SeriesCollectionPlusOne"
    const val Snackbar = "SeriesCollectionSnackbar"
    const val MenuFilter = "SeriesCollectionMenuFilter"
    const val MenuSort = "SeriesCollectionMenuSort"
    const val MenuRefresh = "SeriesCollectionRefresh"
}
