package com.chesire.nekome.app.settings.config.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.InsertPhoto
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.chesire.nekome.app.settings.R
import com.chesire.nekome.core.compose.composables.NekomeDialog
import com.chesire.nekome.core.compose.theme.NekomeTheme
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.preferences.flags.HomeScreenOptions
import com.chesire.nekome.core.preferences.flags.Theme

@Composable
fun ConfigScreen(
    viewModel: ConfigViewModel = hiltViewModel(),
    navigateToOssScreen: () -> Unit,
    navigateAfterLogout: () -> Unit
) {
    val state = viewModel.uiState.collectAsState()
    Render(
        state = state,
        onLogoutClicked = { viewModel.execute(ViewAction.OnLogoutClicked) },
        onLogoutResult = { viewModel.execute(ViewAction.OnLogoutResult(it)) },
        onThemeClicked = { viewModel.execute(ViewAction.OnThemeClicked) },
        onThemeResult = { viewModel.execute(ViewAction.OnThemeChanged(it)) },
        onDefaultHomeScreenClicked = { viewModel.execute(ViewAction.OnDefaultHomeScreenClicked) },
        onDefaultHomeScreenResult = { viewModel.execute(ViewAction.OnDefaultHomeScreenChanged(it)) },
        onDefaultSeriesStatusClicked = { viewModel.execute(ViewAction.OnDefaultSeriesStatusClicked) },
        onDefaultSeriesStatusResult = {
            viewModel.execute(ViewAction.OnDefaultSeriesStatusChanged(it))
        },
        onRateSeriesClicked = { viewModel.execute(ViewAction.OnRateSeriesChanged(it)) },
        onLicensesLinkClicked = { navigateToOssScreen() }
    )

    if (state.value.executeLogout != null) {
        navigateAfterLogout()
        viewModel.execute(ViewAction.ConsumeExecuteLogout)
    }
}

@Composable
private fun Render(
    state: State<UIState>,
    onLogoutClicked: () -> Unit,
    onLogoutResult: (Boolean) -> Unit,
    onThemeClicked: () -> Unit,
    onThemeResult: (Theme?) -> Unit,
    onDefaultHomeScreenClicked: () -> Unit,
    onDefaultHomeScreenResult: (HomeScreenOptions?) -> Unit,
    onDefaultSeriesStatusClicked: () -> Unit,
    onDefaultSeriesStatusResult: (UserSeriesStatus?) -> Unit,
    onRateSeriesClicked: (Boolean) -> Unit,
    onLicensesLinkClicked: () -> Unit
) {
    val scrollableState = rememberScrollState()
    Scaffold(
        modifier = Modifier.semantics { testTag = ConfigTags.Root }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(state = scrollableState)
                .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                .fillMaxSize()
        ) {
            ProfileSection(state.value.userModel, onLogoutClicked)
            ApplicationHeading()
            ThemePreference(state.value.themeValue.stringId, onThemeClicked)
            DefaultHomeScreenPreference(onDefaultHomeScreenClicked)

            SeriesHeading()
            DefaultSeriesStatusPreference(onDefaultSeriesStatusClicked)
            RateSeriesPreference(state.value.rateSeriesValue, onRateSeriesClicked)

            AboutHeading()
            VersionLink()
            GitHubLink()
            LicensesLink(onLicensesLinkClicked)
        }
    }

    if (state.value.showLogoutDialog) {
        NekomeDialog(
            title = stringResource(id = R.string.menu_logout_summary),
            summary = stringResource(id = R.string.menu_logout_prompt_message),
            confirmButton = stringResource(id = R.string.menu_logout_prompt_confirm),
            cancelButton = stringResource(id = R.string.menu_logout_prompt_cancel),
            onConfirmButtonClicked = { onLogoutResult(true) },
            onCancelButtonClicked = { onLogoutResult(false) }
        )
    }

    if (state.value.showThemeDialog) {
        NekomeDialog(
            title = R.string.settings_theme,
            confirmButton = R.string.ok,
            cancelButton = R.string.cancel,
            currentValue = state.value.themeValue,
            allValues = Theme.values().associateWith { stringResource(id = it.stringId) }.toList(),
            onResult = onThemeResult
        )
    }

    if (state.value.showDefaultHomeDialog) {
        NekomeDialog(
            title = R.string.settings_default_home_title,
            confirmButton = R.string.ok,
            cancelButton = R.string.cancel,
            currentValue = state.value.defaultHomeValue,
            allValues = HomeScreenOptions
                .values()
                .associateWith { stringResource(id = it.stringId) }
                .toList(),
            onResult = onDefaultHomeScreenResult
        )
    }

    if (state.value.showDefaultSeriesStatusDialog) {
        NekomeDialog(
            title = R.string.settings_default_series_status_title,
            confirmButton = R.string.ok,
            cancelButton = R.string.cancel,
            currentValue = state.value.defaultSeriesStatusValue,
            allValues = UserSeriesStatus
                .values()
                .filterNot { it == UserSeriesStatus.Unknown }
                .associateWith { stringResource(id = it.stringId) }
                .toList(),
            onResult = onDefaultSeriesStatusResult
        )
    }
}

@Composable
private fun ProfileSection(
    userModel: UserModel?,
    onLogoutClicked: () -> Unit
) {
    if (userModel != null) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(userModel.avatarUrl)
                        .transformations(CircleCropTransformation())
                        .build(),
                    placeholder = rememberVectorPainter(image = Icons.Default.InsertPhoto),
                    error = rememberVectorPainter(image = Icons.Default.BrokenImage),
                    contentDescription = null,
                    modifier = Modifier
                        .sizeIn(48.dp, 48.dp, 74.dp, 74.dp)
                        .padding(end = 8.dp)
                )

                Text(
                    text = userModel.userName,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = onLogoutClicked) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = stringResource(id = R.string.menu_logout)
                    )
                }
            }
        }
    }
}

@Composable
private fun ApplicationHeading() {
    PreferenceHeading(title = stringResource(id = R.string.settings_category_application))
}

@Composable
private fun ThemePreference(@StringRes themeValue: Int, onThemeClicked: () -> Unit) {
    PreferenceSection(
        title = stringResource(id = R.string.settings_theme),
        summary = stringResource(id = themeValue),
        onClick = onThemeClicked
    )
}

@Composable
private fun DefaultHomeScreenPreference(onDefaultHomeScreenClicked: () -> Unit) {
    PreferenceSection(
        title = stringResource(id = R.string.settings_default_home_title),
        summary = stringResource(id = R.string.settings_default_home_summary),
        onClick = onDefaultHomeScreenClicked
    )
}

@Composable
private fun SeriesHeading() {
    PreferenceHeading(title = stringResource(id = R.string.settings_category_series))
}

@Composable
private fun DefaultSeriesStatusPreference(onDefaultSeriesStatusClicked: () -> Unit) {
    PreferenceSection(
        title = stringResource(id = R.string.settings_default_series_status_title),
        summary = stringResource(id = R.string.settings_default_series_status_summary),
        onClick = onDefaultSeriesStatusClicked
    )
}

@Composable
private fun RateSeriesPreference(
    shouldRateSeries: Boolean,
    onRateSeriesClicked: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .clickable {
                onRateSeriesClicked(!shouldRateSeries)
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(verticalArrangement = Arrangement.Center) {
            Text(
                text = stringResource(id = R.string.settings_rate_on_completion_title),
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = stringResource(id = R.string.settings_rate_on_completion_summary),
                style = MaterialTheme.typography.bodySmall
            )
        }
        Checkbox(
            checked = shouldRateSeries,
            onCheckedChange = null
        )
    }
}

@Composable
private fun AboutHeading() {
    PreferenceHeading(title = stringResource(id = R.string.settings_category_about))
}

@Composable
private fun VersionLink() {
    PreferenceSection(
        title = stringResource(id = R.string.settings_version),
        summary = stringResource(id = R.string.version),
        onClick = null
    )
}

@Composable
private fun GitHubLink() {
    val uriHandler = LocalUriHandler.current
    val uri = "https://github.com/Chesire/Nekome"
    PreferenceSection(
        title = stringResource(id = R.string.settings_github),
        summary = uri,
        onClick = { uriHandler.openUri(uri) }
    )
}

@Composable
private fun LicensesLink(onLicensesLinkClicked: () -> Unit) {
    PreferenceSection(
        title = stringResource(id = R.string.settings_licenses),
        summary = null,
        onClick = onLicensesLinkClicked
    )
}

@Composable
private fun PreferenceHeading(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun PreferenceSection(
    title: String,
    summary: String?,
    onClick: (() -> Unit)?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )

        summary?.let {
            Text(
                text = summary,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
@Preview
private fun Preview() {
    val initialState = UIState(
        userModel = UserModel("", "Nekome"),
        showLogoutDialog = false,
        executeLogout = null,
        themeValue = Theme.System,
        showThemeDialog = false,
        defaultHomeValue = HomeScreenOptions.Anime,
        showDefaultHomeDialog = false,
        defaultSeriesStatusValue = UserSeriesStatus.Current,
        showDefaultSeriesStatusDialog = false,
        rateSeriesValue = false
    )
    NekomeTheme(isDarkTheme = true) {
        Render(
            state = produceState(
                initialValue = initialState,
                producer = { value = initialState }
            ),
            onLogoutClicked = { /**/ },
            onLogoutResult = { /**/ },
            onThemeClicked = { /**/ },
            onThemeResult = { /**/ },
            onDefaultHomeScreenClicked = { /**/ },
            onDefaultHomeScreenResult = { /**/ },
            onDefaultSeriesStatusClicked = { /**/ },
            onDefaultSeriesStatusResult = { /**/ },
            onRateSeriesClicked = { /**/ },
            onLicensesLinkClicked = { /**/ }
        )
    }
}

object ConfigTags {
    const val Root = "ConfigRoot"
}
