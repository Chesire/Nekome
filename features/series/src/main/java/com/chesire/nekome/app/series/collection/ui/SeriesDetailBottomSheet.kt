package com.chesire.nekome.app.series.collection.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chesire.nekome.core.compose.theme.NekomeTheme

@Composable
fun SeriesItemBottomSheet(seriesDetails: SeriesDetails?) {
    if (seriesDetails == null) {
        return
    }

    Surface(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
        Box {
            Handle(modifier = Modifier.align(Alignment.TopCenter))
        }
    }
}

@Composable
private fun Handle(modifier: Modifier = Modifier) {
    Divider(
        modifier = modifier.padding(8.dp).width(32.dp)
    )
}

@Composable
private fun Title() {

}

@Composable
private fun Subtitle() {

}

@Composable
private fun SeriesStatus() {

}

@Composable
private fun Progress() {

}

@Composable
private fun Rating() {

}

@Composable
private fun Confirmation() {

}

@Composable
@Preview
private fun Preview() {
    NekomeTheme(darkTheme = true) {
        SeriesItemBottomSheet(
            seriesDetails = SeriesDetails(
                userId = 0,
                title = "Chainsaw Man",
                subtitle = "Anime - TV - Finished",
                userSeriesStatus = "Current",
                progress = 0,
                maxProgress = "12",
                rating = 0
            )
        )
    }
}
