package com.chesire.nekome.app.series.collection.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.chesire.nekome.app.series.R
import com.chesire.nekome.core.compose.theme.NekomeTheme
import com.chesire.nekome.core.flags.SortOption

@Composable
fun SortDialog(
    sortOptions: List<SortOption>,
    onSortResult: (SortOption?) -> Unit
) {
    Render(sortOptions, onSortResult)
}

@Composable
private fun Render(
    options: List<SortOption>,
    onSortResult: (SortOption?) -> Unit
) {
    Dialog(onDismissRequest = { onSortResult(null) }) {
        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(id = R.string.sort_dialog_title))
                options.forEach { sortOption ->
                    ClickableText(
                        text = AnnotatedString(stringResource(id = sortOption.stringId)),
                    ) {
                        onSortResult(sortOption)
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
            listOf(
                SortOption.Default,
                SortOption.Title,
                SortOption.StartDate,
                SortOption.EndDate,
                SortOption.Rating
            ),
            { /**/ }
        )
    }
}
