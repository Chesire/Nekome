package com.chesire.nekome.app.series.collection.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Slider
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.chesire.nekome.core.compose.theme.NekomeTheme
import com.chesire.nekome.resources.StringResource
import kotlin.math.round
import kotlin.math.roundToInt

@Composable
fun RatingDialog(
    ratingDialog: Rating?,
    onRatingComplete: (Series, Int?) -> Unit
) {
    ratingDialog?.let { rating ->
        if (rating.show) {
            Render(
                series = rating.series,
                onRatingResult = { newRating -> onRatingComplete(rating.series, newRating) }
            )
        }
    }
}

@Composable
private fun Render(
    series: Series,
    onRatingResult: (Int?) -> Unit
) {
    var sliderValue by remember { mutableStateOf(series.rating.toFloat()) }
    Dialog(onDismissRequest = { onRatingResult(null) }) {
        Card(modifier = Modifier.semantics { testTag = RatingTags.Root }) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(id = StringResource.rate_dialog_title))
                Slider(
                    value = sliderValue,
                    onValueChange = {
                        sliderValue = if (it == 1f) {
                            0f
                        } else {
                            it
                        }
                    },
                    valueRange = 1f..20f,
                    steps = 18
                )

                Text(
                    modifier = Modifier.padding(8.dp),
                    text = if (sliderValue < 2f) {
                        stringResource(id = StringResource.rating_none)
                    } else {
                        (round(sliderValue) / 2.0).toString()
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        onClick = { onRatingResult(null) }
                    ) {
                        Text(text = stringResource(id = StringResource.series_list_rate_cancel))
                    }
                    TextButton(onClick = { onRatingResult(sliderValue.roundToInt()) }) {
                        Text(text = stringResource(id = StringResource.series_list_rate_confirm))
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun Preview() {
    NekomeTheme(theme = 2) {
        Render(
            series = Series(
                userId = 0,
                title = "",
                posterImageUrl = "",
                subtype = "",
                progress = "",
                startDate = "",
                endDate = "",
                rating = 3,
                showPlusOne = false,
                isUpdating = false
            ),
            onRatingResult = { /**/ }
        )
    }
}

object RatingTags {
    const val Root = "RatingRoot"
}
