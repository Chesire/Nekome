package com.chesire.nekome.feature.serieswidget.ui

import com.chesire.nekome.datasource.series.SeriesDomain
import javax.inject.Inject

class DomainMapper @Inject constructor() {

    fun toSeries(domain: SeriesDomain): Series {
        return Series(
            userId = domain.userId,
            title = domain.title,
            progress = buildProgress(domain.progress, domain.totalLength),
            isUpdating = false
        )
    }

    private fun buildProgress(progress: Int, totalLength: Int): String {
        val maxLengthString = if (totalLength == 0) "-" else totalLength
        return "$progress / $maxLengthString"
    }
}
