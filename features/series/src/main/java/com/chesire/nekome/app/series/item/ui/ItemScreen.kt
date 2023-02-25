package com.chesire.nekome.app.series.item.ui

import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chesire.nekome.core.compose.theme.NekomeTheme
import com.chesire.nekome.core.flags.UserSeriesStatus

@Composable
fun ItemScreen(viewModel: ItemViewModel = viewModel()) {
    val state = viewModel.uiState.collectAsState()
    Render(
        state = state,
        onSeriesStatusChanged = { viewModel.execute(ViewAction.SeriesStatusChanged(it)) },
        onProgressChanged = { viewModel.execute(ViewAction.ProgressChanged(it)) },
        onRatingChanged = { viewModel.execute(ViewAction.RatingChanged(it)) },
        onSnackbarShown = { viewModel.execute(ViewAction.SnackbarObserved) }
    )
}

@Composable
private fun Render(
    state: State<UIState>,
    onSeriesStatusChanged: (UserSeriesStatus) -> Unit,
    onProgressChanged: (Int) -> Unit,
    onRatingChanged: (Int) -> Unit,
    onSnackbarShown: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        // Show details here
    }

    RenderSnackbar(
        snackbarData = state.value.errorSnackbar,
        snackbarHostState = snackbarHostState,
        onSnackbarShown = onSnackbarShown
    )
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
        seriesStatus = UserSeriesStatus.Planned,
        progress = 0,
        length = "-",
        rating = 0,
        isSendingData = false,
        errorSnackbar = null
    )
    NekomeTheme(darkTheme = true) {
        Render(
            state = produceState(
                initialValue = initialState,
                producer = { value = initialState }
            ),
            onSeriesStatusChanged = { /**/ },
            onProgressChanged = { /**/ },
            onRatingChanged = { /**/ },
            onSnackbarShown = { /**/ }
        )
    }
}
