package com.chesire.nekome.app.series.item.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.chesire.nekome.app.series.R
import com.chesire.nekome.core.compose.theme.NekomeTheme

@Composable
fun DeleteDialog(
    deleteDialog: Delete,
    onDeleteResult: (Boolean) -> Unit
) {
    if (deleteDialog.show) {
        Render(
            title = deleteDialog.title,
            onDeleteResult = onDeleteResult
        )
    }
}

@Composable
private fun Render(
    title: String,
    onDeleteResult: (Boolean) -> Unit
) {
    Dialog(onDismissRequest = { onDeleteResult(false) }) {
        Card {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.series_list_delete_title, title),
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = stringResource(R.string.series_list_delete_body),
                    style = MaterialTheme.typography.body1
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        onClick = { onDeleteResult(false) }
                    ) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                    TextButton(onClick = { onDeleteResult(true) }) {
                        Text(text = stringResource(id = R.string.series_list_delete_confirm))
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
            title = "Series Title",
            onDeleteResult = { /**/ }
        )
    }
}
