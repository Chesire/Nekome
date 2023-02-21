package com.chesire.nekome.app.series.collection.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chesire.nekome.core.compose.theme.NekomeTheme

@Composable
fun SeriesItemBottomSheet(seriesDetails: SeriesDetails?) {
    Surface(
        modifier = Modifier
            .size(1.dp)
    ) {
        Box {
            Handle(modifier = Modifier.align(Alignment.TopCenter))
            Title()
        }
    }
}

@Composable
private fun Handle(modifier: Modifier = Modifier) {
    Divider(
        modifier = modifier
            .padding(8.dp)
            .width(32.dp)
    )
}

@Composable
private fun Title() {
    Text(text = "Test")
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
