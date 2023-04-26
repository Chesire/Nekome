@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)

package com.chesire.nekome.app.series.item.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.InsertPhoto
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.chesire.nekome.app.series.R
import com.chesire.nekome.core.compose.composables.NekomeDialog
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
        onLinkPressed = { viewModel.execute(ViewAction.LinkPressed(it)) },
        onLinkDialogResult = { viewModel.execute(ViewAction.OnLinkResult(it)) },
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
    onLinkPressed: (Link) -> Unit,
    onLinkDialogResult: (LinkDialogResult?) -> Unit,
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
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(state = scrollableState)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Title(title = state.value.title)
            Subtitle(subtitle = state.value.subtitle)
            Row(modifier = Modifier.fillMaxWidth()) {
                SeriesImage(imageUrl = state.value.imageUrl)
                SeriesLinks(links = state.value.links, onLinkPressed = onLinkPressed)
            }
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

    // TODO: Get/Set teh links in the DB.
    LinkDialog(
        linkDialogData = state.value.linkDialogData,
        onLinkDialogResult = onLinkDialogResult
    )
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
        style = MaterialTheme.typography.headlineSmall
    )
}

@Composable
private fun Subtitle(subtitle: String) {
    Text(
        text = subtitle,
        style = MaterialTheme.typography.bodySmall
    )
}

@Composable
private fun SeriesImage(imageUrl: String) {
    AsyncImage(
        model = imageUrl,
        placeholder = rememberVectorPainter(image = Icons.Default.InsertPhoto),
        error = rememberVectorPainter(image = Icons.Default.BrokenImage),
        contentDescription = null,
        modifier = Modifier
            .padding(top = 16.dp)
            .sizeIn(minWidth = 48.dp, minHeight = 48.dp, maxWidth = 168.dp, maxHeight = 168.dp)
            .aspectRatio(0.7f)
    )
}

@Composable
private fun SeriesLinks(
    links: List<Link>,
    onLinkPressed: (Link) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .heightIn(min = 48.dp, max = 168.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = links) {
            when (it) {
                Link.AddLink -> SeriesAddLink(onLinkPressed)
                is Link.PopulatedLink -> SeriesPopulatedLink(it, onLinkPressed)
            }
        }
    }
}

@Composable
private fun SeriesAddLink(onLinkPressed: (Link) -> Unit) {
    OutlinedCard(onClick = { onLinkPressed(Link.AddLink) }) {
        Row(
            modifier = Modifier.padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = rememberVectorPainter(image = Icons.Default.Add),
                contentDescription = null
            )
            Text(
                text = stringResource(id = R.string.series_detail_add_link_title),
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 4.dp),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
private fun SeriesPopulatedLink(
    populatedLink: Link.PopulatedLink,
    onLinkPressed: (Link) -> Unit
) {
    val uriHandler = LocalUriHandler.current
    val linkTag = "link"
    val annotatedString = buildAnnotatedString {
        pushStringAnnotation(tag = linkTag, annotation = populatedLink.linkText)
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
            append(populatedLink.title)
        }
        pop()
    }

    OutlinedCard {
        Row(
            modifier = Modifier.padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onLinkPressed(populatedLink) }) {
                // TODO: Unless have an edit button elsewhere which shows a pencil?
                Icon(
                    painter = rememberVectorPainter(image = Icons.Default.Edit),
                    contentDescription = null
                )
            }
            ClickableText(
                text = annotatedString,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 4.dp),
                style = MaterialTheme.typography.labelMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            ) { offset ->
                annotatedString
                    .getStringAnnotations(
                        tag = linkTag,
                        start = offset,
                        end = offset
                    )
                    .firstOrNull()
                    ?.let {
                        // TODO: This must be prepended with "http://" or "https://", update in usecase
                        uriHandler.openUri(it.item)
                    }
            }
        }
    }
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
        text = stringResource(id = R.string.series_detail_progress_title),
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
        style = MaterialTheme.typography.bodyLarge
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
        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
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
private fun DeleteDialog(
    deleteDialog: Delete,
    onDeleteResult: (Boolean) -> Unit
) {
    if (deleteDialog.show) {
        NekomeDialog(
            title = stringResource(id = R.string.series_list_delete_title, deleteDialog.title),
            summary = stringResource(R.string.series_list_delete_body),
            confirmButton = stringResource(id = R.string.series_detail_confirm),
            cancelButton = stringResource(id = R.string.cancel),
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
        links = listOf(
            Link.PopulatedLink("0", "Kitsu page", "http://kitsu.io"),
            Link.PopulatedLink("1", "MAL page", "http://myanimelist.net"),
            Link.AddLink
        ),
        linkDialogData = LinkDialogData(
            show = false,
            link = Link.AddLink
        ),
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
            onLinkPressed = { /**/ },
            onLinkDialogResult = { /**/ },
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
