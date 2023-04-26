package com.chesire.nekome.app.series.item.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chesire.nekome.app.series.R
import com.chesire.nekome.core.compose.composables.NekomeDialog
import com.chesire.nekome.core.compose.theme.NekomeTheme

@Composable
fun LinkDialog(
    linkDialogData: LinkDialogData,
    onLinkDialogResult: (LinkDialogResult?) -> Unit
) {
    if (linkDialogData.show) {
        var titleText by remember {
            mutableStateOf(
                when (linkDialogData.link) {
                    Link.AddLink -> ""
                    is Link.PopulatedLink -> linkDialogData.link.title
                }
            )
        }
        var linkText by remember {
            mutableStateOf(
                when (linkDialogData.link) {
                    Link.AddLink -> ""
                    is Link.PopulatedLink -> linkDialogData.link.linkText
                }
            )
        }
        NekomeDialog(
            title = stringResource(
                id = if (linkDialogData.link is Link.AddLink)
                    R.string.series_detail_add_link_title
                else
                    R.string.series_detail_edit_link_title
            ),
            confirmButton = stringResource(id = R.string.series_detail_confirm),
            cancelButton = stringResource(id = R.string.cancel),
            onConfirmButtonClicked = {
                onLinkDialogResult(
                    LinkDialogResult(
                        link = linkDialogData.link,
                        titleText = titleText,
                        linkText = linkText
                    )
                )
            },
            onCancelButtonClicked = { onLinkDialogResult(null) }
        ) {
            OutlinedTextField(
                value = titleText,
                onValueChange = { titleText = it },
                modifier = Modifier.padding(horizontal = 8.dp),
                label = {
                    Text(text = stringResource(id = R.string.series_detail_link_title_label))
                },
                placeholder = {
                    Text(text = stringResource(id = R.string.series_detail_link_title_placeholder))
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                singleLine = true
            )
            OutlinedTextField(
                value = linkText,
                onValueChange = { linkText = it },
                modifier = Modifier.padding(8.dp),
                label = {
                    Text(text = stringResource(id = R.string.series_detail_link_link_label))
                },
                placeholder = {
                    Text(text = stringResource(id = R.string.series_detail_link_link_placeholder))
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onLinkDialogResult(
                            LinkDialogResult(
                                link = linkDialogData.link,
                                titleText = titleText,
                                linkText = linkText
                            )
                        )
                    }
                ),
                singleLine = true
            )
        }
    }
}

@Composable
@Preview
private fun Preview() {
    NekomeTheme(isDarkTheme = true) {
        LinkDialog(
            linkDialogData = LinkDialogData(
                show = true,
                link = Link.AddLink
            ),
            onLinkDialogResult = { /**/ }
        )
    }
}
