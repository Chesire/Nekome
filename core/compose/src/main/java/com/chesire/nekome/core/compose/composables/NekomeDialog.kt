package com.chesire.nekome.core.compose.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

/**
 * Dialog composable which provides a cancel and confirm button.
 */
@Composable
fun NekomeDialog(
    title: String,
    summary: String?,
    confirmButton: String,
    cancelButton: String,
    onConfirmButtonClicked: () -> Unit,
    onCancelButtonClicked: () -> Unit
) {
    Dialog(onDismissRequest = { onCancelButtonClicked() }) {
        Card(modifier = Modifier.semantics { testTag = DialogTags.Root }) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                summary?.let {
                    Text(
                        text = summary,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .semantics { testTag = DialogTags.CancelButton },
                        onClick = { onCancelButtonClicked() }
                    ) {
                        Text(text = cancelButton)
                    }
                    TextButton(
                        onClick = { onConfirmButtonClicked() },
                        modifier = Modifier.semantics { testTag = DialogTags.OkButton }
                    ) {
                        Text(text = confirmButton)
                    }
                }
            }
        }
    }
}

/**
 * Dialog composable which lays out the values in a vertical list with radio buttons.
 */
@Composable
fun <T> NekomeDialog(
    @StringRes title: Int,
    @StringRes confirmButton: Int,
    @StringRes cancelButton: Int,
    currentValue: T,
    allValues: List<Pair<T, String>>,
    onResult: (T?) -> Unit
) {
    var selectedOption by remember { mutableStateOf(currentValue) }

    Dialog(onDismissRequest = { onResult(null) }) {
        Card(modifier = Modifier.semantics { testTag = DialogTags.Root }) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(id = title),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                allValues.forEach { pair ->
                    val (value, displayValue) = pair
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = value == selectedOption,
                                onClick = {
                                    selectedOption = value
                                }
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            modifier = Modifier.semantics { testTag = DialogTags.OptionRadio },
                            selected = value == selectedOption,
                            onClick = { selectedOption = value }
                        )
                        Text(
                            text = displayValue,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .semantics { testTag = DialogTags.OptionText }
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .semantics { testTag = DialogTags.CancelButton },
                        onClick = { onResult(null) }
                    ) {
                        Text(text = stringResource(id = cancelButton))
                    }
                    TextButton(
                        onClick = { onResult(selectedOption) },
                        modifier = Modifier.semantics { testTag = DialogTags.OkButton }
                    ) {
                        Text(text = stringResource(id = confirmButton))
                    }
                }
            }
        }
    }
}

object DialogTags {
    const val Root = "DialogRoot"
    const val OptionText = "DialogOptionText"
    const val OptionRadio = "DialogOptionRadio"
    const val OkButton = "DialogOkButton"
    const val CancelButton = "DialogCancelButton"
}
