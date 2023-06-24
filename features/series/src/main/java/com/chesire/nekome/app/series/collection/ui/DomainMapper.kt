package com.chesire.nekome.app.series.collection.ui

import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.core.preferences.SeriesPreferences
import com.chesire.nekome.core.preferences.flags.ImageQuality
import com.chesire.nekome.core.preferences.flags.TitleLanguage
import com.chesire.nekome.datasource.series.SeriesDomain
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class DomainMapper @Inject constructor(
    private val seriesPreferences: SeriesPreferences
) {

    fun toSeries(models: List<SeriesDomain>): List<Series> {
        return models.map { series ->
            Series(
                userId = series.userId,
                title = chooseTitle(series),
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

    private fun chooseTitle(series: SeriesDomain): String {
        val titleLanguage = runBlocking {
            seriesPreferences.titleLanguage.first()
        }

        return when (titleLanguage) {
            TitleLanguage.Canonical -> series.title
            else -> series.otherTitles[titleLanguage.key]
                .takeIf { !it.isNullOrBlank() }
                ?: series.title
        }
    }

    private fun choosePosterImageUrl(imageModel: ImageModel): String {
        val imageQuality = runBlocking {
            // Not sure this is the best, but not sure how to get the value otherwise
            seriesPreferences.imageQuality.first()
        }

        return when (imageQuality) {
            ImageQuality.Low -> imageModel.smallest?.url
            ImageQuality.Medium -> imageModel.middlest?.url
            ImageQuality.High -> imageModel.largest?.url
        } ?: ""
    }

    private fun buildProgress(progress: Int, totalLength: Int): String {
        val maxLengthString = if (totalLength == 0) "-" else totalLength
        return "$progress / $maxLengthString"
    }

    private fun shouldShowPlusOne(progress: Int, totalLength: Int): Boolean =
        totalLength == 0 || progress < totalLength
}
