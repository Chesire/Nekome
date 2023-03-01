package com.chesire.nekome.app.series.collection.ui

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.chesire.nekome.app.series.R
import com.chesire.nekome.core.compose.theme.NekomeTheme
import com.chesire.nekome.core.flags.SortOption

@Composable
fun SortDialog(
    sortDialog: Sort,
    onSortResult: (SortOption?) -> Unit
) {
    if (sortDialog.show) {
        Render(
            options = sortDialog.sortOptions,
            currentSort = sortDialog.currentSort,
            onSortResult = onSortResult
        )
    }
}

@Composable
private fun Render(
    options: List<SortOption>,
    currentSort: SortOption,
    onSortResult: (SortOption?) -> Unit
) {
    var selectedOption by remember { mutableStateOf(SortOption.forIndex(currentSort.index)) }

    Dialog(onDismissRequest = { onSortResult(null) }) {
        Card {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.sort_dialog_title),
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                options.forEach { sortOption ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = sortOption == selectedOption,
                                onClick = {
                                    selectedOption = sortOption
                                }
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = sortOption == selectedOption,
                            onClick = { selectedOption = sortOption }
                        )
                        Text(
                            text = stringResource(id = sortOption.stringId),
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        onClick = { onSortResult(null) }
                    ) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                    TextButton(onClick = { onSortResult(selectedOption) }) {
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
            options = listOf(
                SortOption.Default,
                SortOption.Title,
                SortOption.StartDate,
                SortOption.EndDate,
                SortOption.Rating
            ),
            currentSort = SortOption.Default,
            onSortResult = { /**/ }
        )
    }
}
