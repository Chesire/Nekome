@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)

package com.chesire.nekome.app.search.host.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ChipDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FilterChip
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chesire.nekome.app.search.R
import com.chesire.nekome.app.search.host.core.model.SearchGroup
import com.chesire.nekome.core.compose.theme.NekomeTheme

@Composable
fun HostScreen(
    viewModel: HostViewModel = viewModel(),
    navigationAction: (NavigationData) -> Unit
) {
    val state = viewModel.uiState.collectAsState()

    val navigationEvent = state.value.navigateScreenEvent
    if (navigationEvent != null) {
        LaunchedEffect(navigationEvent) {
            navigationAction(navigationEvent)
            viewModel.execute(ViewAction.NavigationObserved)
        }
    }

    Render(
        state = state,
        onInputTextChanged = { viewModel.execute(ViewAction.SearchTextUpdated(it)) },
        onSearchPressed = { viewModel.execute(ViewAction.ExecuteSearch) },
        onSearchGroupSelected = { viewModel.execute(ViewAction.SearchGroupChanged(it)) },
        onSnackbarShown = { viewModel.execute(ViewAction.ErrorSnackbarObserved) }
    )
}

@Composable
private fun Render(
    state: State<UIState>,
    onInputTextChanged: (String) -> Unit,
    onSearchGroupSelected: (SearchGroup) -> Unit,
    onSearchPressed: () -> Unit,
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
        }
    }

    val snackbarMessage = state.value.errorSnackbarMessage
    if (snackbarMessage != null) {
        val message = stringResource(id = snackbarMessage)
        LaunchedEffect(snackbarMessage) {
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
        label = { Text(text = stringResource(id = R.string.search_series_title)) },
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
            modifier = Modifier
                .padding(8.dp, 0.dp, 8.dp, 8.dp)
                .semantics { testTag = HostTags.Anime },
            colors = ChipDefaults.filterChipColors(
                selectedContentColor = MaterialTheme.colors.primary
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.VideoLibrary,
                    contentDescription = null
                )
            }
        ) {
            Text(text = stringResource(id = R.string.search_anime))
        }
        FilterChip(
            selected = selectedGroup == SearchGroup.Manga,
            onClick = { onSearchGroupSelected(SearchGroup.Manga) },
            modifier = Modifier
                .padding(8.dp, 0.dp, 8.dp, 8.dp)
                .semantics { testTag = HostTags.Manga },
            colors = ChipDefaults.filterChipColors(
                selectedContentColor = MaterialTheme.colors.primary
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.CollectionsBookmark,
                    contentDescription = null
                )
            }
        ) {
            Text(text = stringResource(id = R.string.search_manga))
        }
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
        Text(text = stringResource(id = R.string.search_search))
    }
}

@Composable
@Preview
private fun Preview() {
    val initialState = UIState(
        searchText = "Some initial search text",
        isSearchTextError = false,
        searchGroup = SearchGroup.Anime,
        isSearching = false,
        errorSnackbarMessage = null,
        navigateScreenEvent = null
    )
    NekomeTheme(darkTheme = true) {
        Render(
            state = produceState(initialValue = initialState, producer = { value = initialState }),
            onInputTextChanged = { /**/ },
            onSearchPressed = { /**/ },
            onSearchGroupSelected = { /**/ },
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
