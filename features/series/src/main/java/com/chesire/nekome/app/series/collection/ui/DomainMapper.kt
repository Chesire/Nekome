package com.chesire.nekome.app.series.collection.ui

import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.datasource.series.SeriesDomain
import javax.inject.Inject

class DomainMapper @Inject constructor() {

    fun toSeries(models: List<SeriesDomain>): List<Series> {
        return models.map { series ->
            Series(
                userId = series.userId,
                title = series.title,
                posterImageUrl = choosePosterImageUrl(series.posterImage),
                subtype = series.subtype.name,
                progress = buildProgress(series.progress, series.totalLength),
                startDate = series.startDate,
                endDate = series.endDate,
                rating = series.rating,
                showPlusOne = shouldShowPlusOne(series.progress, series.totalLength),
                isUpdating = false
            )
        }
    }

    private fun choosePosterImageUrl(imageModel: ImageModel): String {
        return imageModel.smallest?.url ?: ""
    }

    private fun buildProgress(progress: Int, totalLength: Int): String {
        val maxLengthString = if (totalLength == 0) "-" else totalLength
        return "$progress / $maxLengthString"
    }

    private fun shouldShowPlusOne(progress: Int, totalLength: Int): Boolean {
        return totalLength == 0 || progress < totalLength
    }
}
