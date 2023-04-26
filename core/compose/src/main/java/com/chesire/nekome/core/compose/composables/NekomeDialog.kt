package com.chesire.nekome.core.compose.composables

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
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun NekomeDialog(
    title: String,
    confirmButton: String,
    cancelButton: String,
    onConfirmButtonClicked: () -> Unit,
    onCancelButtonClicked: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(onDismissRequest = { onCancelButtonClicked() }) {
        Card(modifier = Modifier.semantics { testTag = DialogTags.Root }) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                content()

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
    NekomeDialog(
        title = title,
        confirmButton = confirmButton,
        cancelButton = cancelButton,
        onConfirmButtonClicked = onConfirmButtonClicked,
        onCancelButtonClicked = onCancelButtonClicked
    ) {
        summary?.let {
            Text(
                text = summary,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}

/**
 * Dialog composable which lays out the values in a vertical list with radio buttons.
 */
@Composable
fun <T> NekomeDialog(
    title: String,
    confirmButton: String,
    cancelButton: String,
    currentValue: T,
    allValues: List<Pair<T, String>>,
    onResult: (T?) -> Unit
) {
    var selectedOption by remember { mutableStateOf(currentValue) }

    NekomeDialog(
        title = title,
        confirmButton = confirmButton,
        cancelButton = cancelButton,
        onConfirmButtonClicked = { onResult(selectedOption) },
        onCancelButtonClicked = { onResult(null) }
    ) {
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
    }
}

object DialogTags {
    const val Root = "DialogRoot"
    const val OptionText = "DialogOptionText"
    const val OptionRadio = "DialogOptionRadio"
    const val OkButton = "DialogOkButton"
    const val CancelButton = "DialogCancelButton"
}
