package com.chesire.nekome.app.settings.config.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.chesire.nekome.core.compose.theme.NekomeTheme
import com.chesire.nekome.core.preferences.flags.Theme
import com.chesire.nekome.app.settings.R

@Composable
fun ThemeDialog(
    options: ThemeDialogOptions,
    onThemeResult: (Theme?) -> Unit
) {
    if (options.show) {
        Render(
            currentTheme = options.currentTheme,
            onThemeResult = onThemeResult
        )
    }
}

@Composable
private fun Render(
    currentTheme: Theme,
    onThemeResult: (Theme?) -> Unit
) {
    var selectedOption by remember { mutableStateOf(currentTheme) }

    // Move this into a generic NekomeDialog class, likely with T
    Dialog(onDismissRequest = { onThemeResult(null) }) {
        Card(modifier = Modifier.semantics { testTag = ThemeTags.Root }) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(id = R.string.settings_theme),
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Theme.values().forEach { theme ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = theme == selectedOption,
                                onClick = {
                                    selectedOption = theme
                                }
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = theme == selectedOption,
                            onClick = { selectedOption = theme }
                        )
                        Text(
                            text = stringResource(id = theme.stringId),
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .semantics { testTag = ThemeTags.OptionText }
                        )
                    }
                }


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        onClick = { onThemeResult(null) }
                    ) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                    TextButton(
                        onClick = { onThemeResult(selectedOption) },
                        modifier = Modifier.semantics { testTag = ThemeTags.OkButton }
                    ) {
                        Text(text = stringResource(id = R.string.ok))
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun Preview() {
    NekomeTheme(darkTheme = true) {
        Render(
            currentTheme = Theme.System,
            onThemeResult = { /**/ }
        )
    }
}

data class ThemeDialogOptions(
    val show: Boolean,
    val currentTheme: Theme
)
object ThemeTags {
    const val Root = "ThemeRoot"
    const val OptionText = "ThemeOptionText"
    const val OkButton = "ThemeOkButton"
}
