@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)

package com.chesire.nekome.app.search.host.ui

import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.InsertPhoto
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.chesire.nekome.app.search.host.core.model.SearchGroup
import com.chesire.nekome.core.compose.theme.NekomeTheme
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.resources.StringResource

@Composable
fun HostScreen(viewModel: HostViewModel = hiltViewModel()) {
    val state = viewModel.uiState.collectAsState()

    Render(
        state = state,
        onInputTextChanged = { viewModel.execute(ViewAction.SearchTextUpdated(it)) },
        onSearchPressed = { viewModel.execute(ViewAction.ExecuteSearch) },
        onSearchGroupSelected = { viewModel.execute(ViewAction.SearchGroupChanged(it)) },
        onSeriesTrack = { viewModel.execute(ViewAction.TrackSeries(it)) },
        onSnackbarShown = { viewModel.execute(ViewAction.ErrorSnackbarObserved) }
    )
}

@Composable
private fun Render(
    state: State<UIState>,
    onInputTextChanged: (String) -> Unit,
    onSearchGroupSelected: (SearchGroup) -> Unit,
    onSearchPressed: () -> Unit,
    onSeriesTrack: (ResultModel) -> Unit,
    onSnackbarShown: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.semantics { testTag = HostTags.Snackbar }
            )
        },
        modifier = Modifier.semantics { testTag = HostTags.Root }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InputText(state.value.searchText, state.value.isSearchTextError, onInputTextChanged)
            SearchGroup(state.value.searchGroup, onSearchGroupSelected)
            if (state.value.isSearching) {
                CircularProgressIndicator()
            } else {
                SearchButton(state.value.isSearching, onSearchPressed)
            }
            Divider(modifier = Modifier.padding(8.dp))
            SearchResults(state.value.resultModels, onSeriesTrack)
        }
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
private fun InputText(text: String, isError: Boolean, onInputTextChanged: (String) -> Unit) {
    OutlinedTextField(
        value = text,
        onValueChange = onInputTextChanged,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            autoCorrect = false
        ),
        singleLine = true,
        label = { Text(text = stringResource(id = StringResource.search_series_title)) },
        isError = isError,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .semantics { testTag = HostTags.Input }
    )
}

@Composable
private fun SearchGroup(
    selectedGroup: SearchGroup,
    onSearchGroupSelected: (SearchGroup) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        FilterChip(
            selected = selectedGroup == SearchGroup.Anime,
            onClick = { onSearchGroupSelected(SearchGroup.Anime) },
            label = { Text(text = stringResource(id = StringResource.search_anime)) },
            modifier = Modifier
                .padding(8.dp, 0.dp, 8.dp, 8.dp)
                .semantics { testTag = HostTags.Anime },
            colors = FilterChipDefaults.filterChipColors(
                selectedLabelColor = MaterialTheme.colorScheme.primary
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.VideoLibrary,
                    contentDescription = null
                )
            }
        )
        FilterChip(
            selected = selectedGroup == SearchGroup.Manga,
            onClick = { onSearchGroupSelected(SearchGroup.Manga) },
            label = { Text(text = stringResource(id = StringResource.search_manga)) },
            modifier = Modifier
                .padding(8.dp, 0.dp, 8.dp, 8.dp)
                .semantics { testTag = HostTags.Manga },
            colors = FilterChipDefaults.filterChipColors(
                selectedLabelColor = MaterialTheme.colorScheme.primary
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.CollectionsBookmark,
                    contentDescription = null
                )
            }
        )
    }
}

@Composable
private fun SearchButton(isSearching: Boolean, onSearchPressed: () -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Button(
        onClick = {
            if (!isSearching) {
                onSearchPressed()
                keyboardController?.hide()
            }
        },
        modifier = Modifier
            .padding(16.dp)
            .semantics { testTag = HostTags.Search }
    ) {
        Text(text = stringResource(id = StringResource.search_search))
    }
}

@Composable
private fun SearchResults(
    resultModels: List<ResultModel>,
    onSeriesTrack: (ResultModel) -> Unit
) {
    if (resultModels.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
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
                    model = model.posterImage,
                    placeholder = rememberVectorPainter(image = Icons.Default.InsertPhoto),
                    error = rememberVectorPainter(image = Icons.Default.BrokenImage),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(0.7f)
                        .align(Alignment.CenterVertically),
                    contentScale = ContentScale.FillBounds
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
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = model.synopsis,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = model.subtype,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            if (model.canTrack && !model.isTracking) {
                IconButton(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    onClick = { onSeriesTrack(model) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(
                            id = StringResource.results_track_series
                        ),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.border(
                            1.dp,
                            MaterialTheme.colorScheme.primary,
                            CircleShape
                        )
                    )
                }
            }
        }
    }
}

@Composable
@Preview("Preview")
private fun Preview() {
    val initialState = UIState(
        searchText = "Some initial search text",
        isSearchTextError = false,
        searchGroup = SearchGroup.Anime,
        isSearching = false,
        resultModels = emptyList(),
        errorSnackbar = null
    )
    NekomeTheme(isDarkTheme = true) {
        Render(
            state = produceState(initialValue = initialState, producer = { value = initialState }),
            onInputTextChanged = { /**/ },
            onSearchPressed = { /**/ },
            onSearchGroupSelected = { /**/ },
            onSeriesTrack = { /**/ },
            onSnackbarShown = { /**/ }
        )
    }
}

@Composable
@Preview("Populated preview")
private fun PopulatedPreview() {
    val initialState = UIState(
        searchText = "Some initial search text",
        isSearchTextError = false,
        searchGroup = SearchGroup.Anime,
        isSearching = false,
        resultModels = listOf(
            ResultModel(
                id = 0,
                type = SeriesType.Anime,
                synopsis = "This is a synopsis",
                title = "This is the title",
                subtype = "Oneshot",
                posterImage = "",
                canTrack = true,
                isTracking = false
            ),
            ResultModel(
                id = 1,
                type = SeriesType.Anime,
                synopsis = "This is another synopsis",
                title = "This is the title again",
                subtype = "Oneshot",
                posterImage = "",
                canTrack = false,
                isTracking = true
            )
        ),
        errorSnackbar = null
    )
    NekomeTheme(isDarkTheme = true) {
        Render(
            state = produceState(initialValue = initialState, producer = { value = initialState }),
            onInputTextChanged = { /**/ },
            onSearchPressed = { /**/ },
            onSearchGroupSelected = { /**/ },
            onSeriesTrack = { /**/ },
            onSnackbarShown = { /**/ }
        )
    }
}

object HostTags {
    const val Root = "HostRoot"
    const val Input = "HostInput"
    const val Anime = "HostAnime"
    const val Manga = "HostManga"
    const val Search = "HostSearch"
    const val Snackbar = "HostSnackbar"
}
