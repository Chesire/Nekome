package com.chesire.nekome.app.series.collection.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog

@Composable
fun FilterDialog(
    filterDialog: Filter,
    onFilterResult: () -> Unit
) {
    if (filterDialog.show) {
        Render(
            onFilterResult = onFilterResult
        )
    }
}

@Composable
private fun Render(
    onFilterResult: () -> Unit
) {
    Dialog(onDismissRequest = { onFilterResult() }) {
        Card {
            Column {
                Text(text = "Test")
            }
        }
    }
}
