package com.chesire.nekome.app.series.collection.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.chesire.nekome.app.series.R
import com.chesire.nekome.core.compose.theme.NekomeTheme
import com.chesire.nekome.core.flags.UserSeriesStatus

@Composable
fun FilterDialog(
    filterDialog: Filter,
    onFilterResult: (List<FilterOption>?) -> Unit
) {
    if (filterDialog.show) {
        Render(
            filters = filterDialog.filterOptions,
            onFilterResult = onFilterResult
        )
    }
}

@Composable
private fun Render(
    filters: List<FilterOption>,
    onFilterResult: (List<FilterOption>?) -> Unit
) {
    val selectedFilters = remember {
        filters.map { it.userStatus to it.selected }.toMutableStateMap()
    }
    Dialog(onDismissRequest = { onFilterResult(null) }) {
        Card(modifier = Modifier.semantics { testTag = FilterTags.Root }) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.filter_dialog_title),
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                filters.forEach { filter ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = selectedFilters[filter.userStatus] == true,
                                onClick = {
                                    selectedFilters[filter.userStatus] =
                                        !selectedFilters.getValue(filter.userStatus)
                                }
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            modifier = Modifier.semantics { testTag = FilterTags.OptionChecked },
                            checked = selectedFilters[filter.userStatus] == true,
                            onCheckedChange = { checkValue ->
                                selectedFilters[filter.userStatus] = checkValue
                            }
                        )
                        Text(
                            text = stringResource(id = filter.userStatus.stringId),
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .semantics { testTag = FilterTags.OptionText }
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
                            .semantics { testTag = FilterTags.CancelButton },
                        onClick = { onFilterResult(null) }
                    ) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                    TextButton(
                        modifier = Modifier.semantics { testTag = FilterTags.OkButton },
                        onClick = {
                            onFilterResult(
                                selectedFilters.map {
                                    FilterOption(userStatus = it.key, selected = it.value)
                                }
                            )
                        },
                        enabled = selectedFilters.any { it.value }
                    ) {
                        Text(text = stringResource(id = R.string.ok))
                    }
                }
            }
        }
    }
}

data class FilterOption(
    val userStatus: UserSeriesStatus,
    val selected: Boolean
)

@Composable
@Preview
private fun Preview() {
    NekomeTheme(darkTheme = true) {
        Render(
            filters = listOf(
                FilterOption(UserSeriesStatus.Current, true),
                FilterOption(UserSeriesStatus.Completed, false),
                FilterOption(UserSeriesStatus.OnHold, true),
                FilterOption(UserSeriesStatus.Dropped, false),
                FilterOption(UserSeriesStatus.Planned, true)
            ),
            onFilterResult = { /**/ }
        )
    }
}

object FilterTags {
    const val Root = "FilterRoot"
    const val OptionChecked = "FilterOptionChecked"
    const val OptionText = "FilterOptionText"
    const val OkButton = "FilterOkButton"
    const val CancelButton = "FilterCancelButton"
}
