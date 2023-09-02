@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)

package com.chesire.nekome.app.series.item.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.InsertPhoto
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.chesire.nekome.core.compose.composables.NekomeDialog
import com.chesire.nekome.core.compose.theme.NekomeTheme
import com.chesire.nekome.core.compose.verticalFadingEdge
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.resources.StringResource
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import kotlin.math.round

@Composable
fun ItemScreen(
    viewModel: ItemViewModel = hiltViewModel(),
    finishScreen: () -> Unit
) {
    val state = viewModel.uiState.collectAsState()
    Render(
        state = state,
        onSeriesStatusChanged = { viewModel.execute(ViewAction.SeriesStatusChanged(it)) },
        onProgressChanged = { viewModel.execute(ViewAction.ProgressChanged(it)) },
        onRatingChanged = { viewModel.execute(ViewAction.RatingChanged(it)) },
        onConfirmPressed = { viewModel.execute(ViewAction.ConfirmPressed) },
        onDeletePressed = { viewModel.execute(ViewAction.DeletePressed) },
        onDeleteResult = { viewModel.execute(ViewAction.OnDeleteResult(it)) },
        onSnackbarShown = { viewModel.execute(ViewAction.SnackbarObserved) },
        onFinishedScreen = { viewModel.execute(ViewAction.FinishScreenObserved) },
        finishScreen = finishScreen
    )
}

@Composable
private fun Render(
    state: State<UIState>,
    onSeriesStatusChanged: (UserSeriesStatus) -> Unit,
    onProgressChanged: (String) -> Unit,
    onRatingChanged: (Float) -> Unit,
    onConfirmPressed: () -> Unit,
    onDeletePressed: () -> Unit,
    onDeleteResult: (Boolean) -> Unit,
    onSnackbarShown: () -> Unit,
    finishScreen: () -> Unit,
    onFinishedScreen: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollableState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            finishScreen()
                            onFinishedScreen()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(id = StringResource.back)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onDeletePressed) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(id = StringResource.series_detail_delete)
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(state = scrollableState)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            HeaderArea(
                title = state.value.title,
                subtitle = state.value.subtitle,
                imageUrl = state.value.imageUrl
            )
            Spacer(modifier = Modifier.height(4.dp))
            SeriesStatus(
                possibleSeriesStatus = state.value.possibleSeriesStatus,
                seriesStatus = state.value.seriesStatus,
                onSeriesStatusChanged = onSeriesStatusChanged
            )
            Progress(
                progress = state.value.progress,
                length = state.value.length,
                onProgressChanged = onProgressChanged
            )
            Rating(
                rating = state.value.rating,
                onRatingChanged = onRatingChanged
            )
            Spacer(modifier = Modifier.weight(1f))
            if (state.value.isSendingData) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ConfirmButton(
                        isSendingData = state.value.isSendingData,
                        onConfirmPressed = onConfirmPressed
                    )
                }
            }
        }
    }

    DeleteDialog(
        deleteDialog = state.value.deleteDialog,
        onDeleteResult = onDeleteResult
    )
    RenderSnackbar(
        snackbarData = state.value.errorSnackbar,
        snackbarHostState = snackbarHostState,
        onSnackbarShown = onSnackbarShown
    )

    if (state.value.finishScreen) {
        finishScreen()
        onFinishedScreen()
    }
}

@Composable
private fun HeaderArea(
    title: String,
    subtitle: String,
    imageUrl: String
) {
    val scrollState = rememberScrollState()
    var componentHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    Row {
        SeriesImage(
            image = imageUrl,
            modifier = Modifier
                .onGloballyPositioned {
                    componentHeight = with(density) {
                        it.size.height.toDp()
                    }
                }
        )
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .height(componentHeight)
                .verticalFadingEdge(scrollState, 32.dp)
                .verticalScroll(scrollState)
        ) {
            Title(title = title)
            Subtitle(subtitle = subtitle)
        }
    }
}

@Composable
private fun Title(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun Subtitle(subtitle: String) {
    Text(
        text = subtitle,
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun SeriesImage(
    image: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = image,
        placeholder = rememberVectorPainter(image = Icons.Default.InsertPhoto),
        error = rememberVectorPainter(image = Icons.Default.BrokenImage),
        contentDescription = null,
        modifier = modifier
            .fillMaxWidth(0.4f)
            .aspectRatio(0.7f)
    )
}

@Composable
private fun SeriesStatus(
    possibleSeriesStatus: List<UserSeriesStatus>,
    seriesStatus: UserSeriesStatus,
    onSeriesStatusChanged: (UserSeriesStatus) -> Unit
) {
    Text(
        text = stringResource(id = StringResource.series_detail_status_title),
        modifier = Modifier.padding(top = 16.dp),
        style = MaterialTheme.typography.bodyLarge
    )
    FlowRow(
        mainAxisAlignment = FlowMainAxisAlignment.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 8.dp)
    ) {
        possibleSeriesStatus.forEach { seriesChip ->
            FilterChip(
                selected = seriesChip == seriesStatus,
                label = { Text(text = stringResource(id = seriesChip.stringId)) },
                colors = FilterChipDefaults.filterChipColors(
                    labelColor = MaterialTheme.colorScheme.primary
                ),
                onClick = { onSeriesStatusChanged(seriesChip) }
            )
        }
    }
}

@Composable
private fun Progress(
    progress: String,
    length: String,
    onProgressChanged: (String) -> Unit
) {
    Text(
        text = stringResource(id = StringResource.series_detail_progress_title),
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
        style = MaterialTheme.typography.bodyLarge
    )
    OutlinedTextField(
        value = progress,
        onValueChange = onProgressChanged,
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            Text(
                text = stringResource(
                    id = StringResource.series_detail_progress_out_of,
                    length
                )
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
    )
}

@Composable
private fun Rating(
    rating: Float,
    onRatingChanged: (Float) -> Unit
) {
    Text(
        text = stringResource(id = StringResource.series_detail_rating),
        modifier = Modifier.padding(top = 16.dp),
        style = MaterialTheme.typography.bodyLarge
    )
    Slider(
        value = rating,
        onValueChange = {
            onRatingChanged(
                if (it == 1f) {
                    0f
                } else {
                    it
                }
            )
        },
        valueRange = 1f..20f,
        steps = 18,
        modifier = Modifier.padding(horizontal = 8.dp)
    )

    Text(
        text = if (rating < 2f) {
            stringResource(id = StringResource.rating_none)
        } else {
            (round(rating) / 2.0).toString()
        },
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

@Composable
private fun ConfirmButton(
    isSendingData: Boolean,
    onConfirmPressed: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Button(
        onClick = {
            if (!isSendingData) {
                onConfirmPressed()
                keyboardController?.hide()
            }
        },
        modifier = Modifier.padding(start = 16.dp, top = 32.dp, end = 16.dp, bottom = 0.dp),
        contentPadding = PaddingValues(horizontal = 48.dp, vertical = 16.dp)
    ) {
        Text(text = stringResource(id = StringResource.series_detail_confirm))
    }
}

@Composable
private fun DeleteDialog(
    deleteDialog: Delete,
    onDeleteResult: (Boolean) -> Unit
) {
    if (deleteDialog.show) {
        NekomeDialog(
            title = stringResource(
                id = StringResource.series_list_delete_title,
                deleteDialog.title
            ),
            summary = stringResource(StringResource.series_list_delete_body),
            confirmButton = stringResource(id = StringResource.series_list_delete_confirm),
            cancelButton = stringResource(id = StringResource.cancel),
            onConfirmButtonClicked = { onDeleteResult(true) },
            onCancelButtonClicked = { onDeleteResult(false) }
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
        id = 0,
        title = "Title",
        subtitle = "Anime - TV - Finished",
        imageUrl = "",
        possibleSeriesStatus = listOf(
            UserSeriesStatus.Current,
            UserSeriesStatus.Completed,
            UserSeriesStatus.Dropped,
            UserSeriesStatus.OnHold,
            UserSeriesStatus.Planned
        ),
        seriesStatus = UserSeriesStatus.Planned,
        progress = "0",
        length = "12",
        rating = 0f,
        isSendingData = false,
        finishScreen = false,
        deleteDialog = Delete(
            show = false,
            title = "Title"
        ),
        errorSnackbar = null
    )
    NekomeTheme(isDarkTheme = true) {
        Render(
            state = produceState(
                initialValue = initialState,
                producer = { value = initialState }
            ),
            onSeriesStatusChanged = { /**/ },
            onProgressChanged = { /**/ },
            onRatingChanged = { /**/ },
            onConfirmPressed = { /**/ },
            onDeletePressed = { /**/ },
            onDeleteResult = { /**/ },
            onSnackbarShown = { /**/ },
            finishScreen = { /**/ },
            onFinishedScreen = { /**/ }
        )
    }
}
