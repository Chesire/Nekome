package com.chesire.nekome.app.series.collection.ui

import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.chesire.nekome.core.compose.theme.NekomeTheme

@Composable
fun RatingDialog(
    series: Series,
    onRatingComplete: (Series, Int?) -> Unit
) {
    Render(
        series = series,
        onRatingResult = { newRating -> onRatingComplete(series, newRating) }
    )
}

@Composable
private fun Render(
    series: Series,
    onRatingResult: (Int?) -> Unit
) {
    Dialog(onDismissRequest = { onRatingResult(null) }) {
        Card {

        }
    }
}

@Composable
@Preview
private fun Preview() {
    NekomeTheme(darkTheme = true) {
        Render(
            series = Series(
                userId = 0,
                title = "",
                posterImageUrl = "",
                subtype = "",
                progress = "",
                startDate = "",
                endDate = "",
                showPlusOne = false,
                isUpdating = false

            ),
            onRatingResult = { /**/ },
        )
    }
}
