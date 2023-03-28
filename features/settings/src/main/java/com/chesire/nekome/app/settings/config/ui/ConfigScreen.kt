package com.chesire.nekome.app.settings.config.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chesire.nekome.app.settings.R
import com.chesire.nekome.core.compose.theme.NekomeTheme

@Composable
fun ConfigScreen(
    viewModel: ConfigViewModel = viewModel(),
    navigateToOssScreen: () -> Unit
) {
    val state = viewModel.uiState.collectAsState()
    Render(
        state = state,
        onThemeClicked = { /*TODO*/ },
        onDefaultHomeScreenClicked = { /*TODO*/ },
        onDefaultSeriesStatusClicked = { /*TODO*/ },
        onRateSeriesClicked = { viewModel.execute(ViewAction.OnRateSeriesChanged(it)) },
        onLicensesLinkClicked = { navigateToOssScreen() }
    )
}

@Composable
private fun Render(
    state: State<UIState>,
    onThemeClicked: () -> Unit,
    onDefaultHomeScreenClicked: () -> Unit,
    onDefaultSeriesStatusClicked: () -> Unit,
    onRateSeriesClicked: (Boolean) -> Unit,
    onLicensesLinkClicked: () -> Unit
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            ApplicationHeading()
            ThemePreference(state.value.themeStringDisplay, onThemeClicked)
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
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(verticalArrangement = Arrangement.Center) {
            Text(
                text = stringResource(id = R.string.settings_rate_on_completion_title),
                style = MaterialTheme.typography.subtitle1
            )

            Text(
                text = stringResource(id = R.string.settings_rate_on_completion_summary),
                style = MaterialTheme.typography.caption
            )
        }
        Checkbox(
            checked = shouldRateSeries,
            onCheckedChange = { onRateSeriesClicked(!shouldRateSeries) }
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
        style = MaterialTheme.typography.h6,
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth(),
        color = MaterialTheme.colors.primary
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
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .clickable(enabled = onClick != null) { onClick?.invoke() },
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.subtitle1
        )

        summary?.let {
            Text(
                text = summary,
                style = MaterialTheme.typography.caption
            )
        }
    }
}

@Composable
@Preview
private fun Preview() {
    val initialState = UIState(
        themeStringDisplay = 0,
        rateSeriesValue = false
    )
    NekomeTheme(darkTheme = true) {
        Render(
            state = produceState(
                initialValue = initialState,
                producer = { value = initialState }
            ),
            onThemeClicked = { /*TODO*/ },
            onDefaultHomeScreenClicked = { /*TODO*/ },
            onDefaultSeriesStatusClicked = { /*TODO*/ },
            onRateSeriesClicked = { /*TODO*/ },
            onLicensesLinkClicked = { /**/ }
        )
    }
}
