package com.chesire.nekome.app.search.host.ui

import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.core.preferences.SeriesPreferences
import com.chesire.nekome.core.preferences.flags.ImageQuality
import com.chesire.nekome.core.preferences.flags.TitleLanguage
import com.chesire.nekome.datasource.search.SearchDomain
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class DomainMapper @Inject constructor(
    private val seriesPreferences: SeriesPreferences
) {

    fun toResultModels(models: List<SearchDomain>, currentSeriesIds: List<Int>): List<ResultModel> {
        return models.map { model ->
            ResultModel(
                id = model.id,
                type = model.type,
                synopsis = model.synopsis,
                title = chooseTitle(model),
                subtype = model.subtype.name,
                posterImage = choosePosterImageUrl(model.posterImage),
                canTrack = !currentSeriesIds.contains(model.id),
                isTracking = false
            )
        }
    }

    private fun chooseTitle(series: SearchDomain): String {
        val titleLanguage = runBlocking {
            seriesPreferences.titleLanguage.first()
        }

        return when (titleLanguage) {
            TitleLanguage.Canonical -> series.canonicalTitle
            else -> series.otherTitles[titleLanguage.key]
                .takeIf { !it.isNullOrBlank() }
                ?: series.canonicalTitle
        }
    }

    private fun choosePosterImageUrl(imageModel: ImageModel): String {
        val imageQuality = runBlocking {
            seriesPreferences.imageQuality.first()
        }

        return when (imageQuality) {
            ImageQuality.Low -> imageModel.smallest?.url
            ImageQuality.Medium -> imageModel.middlest?.url
            ImageQuality.High -> imageModel.largest?.url
        } ?: ""
    }
}
