@file:OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)

package com.chesire.nekome.app.series.item.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ChipDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FilterChip
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Slider
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chesire.nekome.app.series.R
import com.chesire.nekome.core.compose.theme.NekomeTheme
import com.chesire.nekome.core.flags.UserSeriesStatus
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

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Title(title = state.value.title)
            Subtitle(subtitle = state.value.subtitle)
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
                    DeleteButton(onDeletePressed = onDeletePressed)
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
private fun Title(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.h5
    )
}

@Composable
private fun Subtitle(subtitle: String) {
    Text(
        text = subtitle,
        style = MaterialTheme.typography.caption
    )
}

@Composable
private fun SeriesStatus(
    possibleSeriesStatus: List<UserSeriesStatus>,
    seriesStatus: UserSeriesStatus,
    onSeriesStatusChanged: (UserSeriesStatus) -> Unit
) {
    Text(
        text = stringResource(id = R.string.series_detail_status_title),
        modifier = Modifier.padding(top = 16.dp),
        style = MaterialTheme.typography.body1
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
                colors = ChipDefaults.filterChipColors(
                    selectedContentColor = MaterialTheme.colors.primary
                ),
                onClick = { onSeriesStatusChanged(seriesChip) }
            ) {
                Text(text = stringResource(id = seriesChip.stringId))
            }
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
        text = stringResource(id = R.string.series_detail_progress_title),
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
        style = MaterialTheme.typography.body1
    )
    OutlinedTextField(
        value = progress,
        onValueChange = onProgressChanged,
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            Text(text = stringResource(id = R.string.series_detail_progress_out_of, length))
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
        text = stringResource(id = R.string.series_detail_rating),
        modifier = Modifier.padding(top = 16.dp),
        style = MaterialTheme.typography.body1
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
            stringResource(id = R.string.rating_none)
        } else {
            (round(rating) / 2.0).toString()
        },
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

@Composable
private fun DeleteButton(onDeletePressed: () -> Unit) {
    Button(
        onClick = { onDeletePressed() },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 0.dp)
    ) {
        Text(text = stringResource(id = R.string.series_detail_delete))
    }
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
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 0.dp)
    ) {
        Text(text = stringResource(id = R.string.series_detail_confirm))
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
