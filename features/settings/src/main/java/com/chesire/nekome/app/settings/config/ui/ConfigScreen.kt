package com.chesire.nekome.app.settings.config.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chesire.nekome.app.settings.R
import com.chesire.nekome.core.compose.theme.NekomeTheme

@Composable
fun ConfigScreen(viewModel: ConfigViewModel = viewModel()) {
    val state = viewModel.uiState.collectAsState()
    Render(
        state = state
    )
}

@Composable
private fun Render(
    state: State<UIState>
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            ApplicationHeading()
            ThemePreference()
            DefaultHomeScreenPreference()

            SeriesHeading()
            DefaultSeriesStatusPreference()
            RateSeriesPreference()

            AboutHeading()
            VersionLink()
            GitHubLink()
            LicensesLink()
        }
    }
}

@Composable
private fun ApplicationHeading() {
    PreferenceHeading(title = stringResource(id = R.string.settings_category_application))
}

@Composable
private fun ThemePreference() {
    PreferenceSection(
        title = stringResource(id = R.string.settings_theme),
        summary = "Not set" // TODO
    ) {
        // TODO
    }
}

@Composable
private fun DefaultHomeScreenPreference() {
    PreferenceSection(
        title = stringResource(id = R.string.settings_default_home_title),
        summary = stringResource(id = R.string.settings_default_home_summary)
    ) {
        // TODO
    }
}

@Composable
private fun SeriesHeading() {
    PreferenceHeading(title = stringResource(id = R.string.settings_category_series))
}

@Composable
private fun DefaultSeriesStatusPreference() {
    PreferenceSection(
        title = stringResource(id = R.string.settings_default_series_status_title),
        summary = stringResource(id = R.string.settings_default_series_status_summary)
    ) {
        // TODO
    }
}

@Composable
private fun RateSeriesPreference() {

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
    PreferenceSection(
        title = stringResource(id = R.string.settings_github),
        summary = stringResource(id = R.string.settings_github_url)
    ) {
        // TODO: Launch browser
    }
}

@Composable
private fun LicensesLink() {
    PreferenceSection(
        title = stringResource(id = R.string.settings_licenses),
        summary = null
    ) {
        // TODO: Navigate to oss screen
    }
}

@Composable
private fun PreferenceHeading(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.h6,
        modifier = Modifier.padding(top = 16.dp),
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
        title = "Title"
    )
    NekomeTheme(darkTheme = true) {
        Render(
            state = produceState(
                initialValue = initialState,
                producer = { value = initialState }
            )
        )
    }
}
