package com.chesire.nekome.app.series.detail

import com.chesire.nekome.app.series.list.lengthKnown
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.library.SeriesDomain

/**
 * Provides an object based on [SeriesDomain] that allows modifying values for series detail.
 */
data class MutableSeriesModel(
    val userSeriesId: Int,
    val seriesName: String,
    var seriesProgress: Int,
    var seriesLengthValue: Int,
    val seriesLength: String,
    val seriesType: String,
    val seriesSubType: String,
    var userSeriesStatus: UserSeriesStatus,
    val seriesStatus: String,
    var rating: Float
) {
    companion object {
        /**
         * Creates an instance of [MutableSeriesModel] from an instance of [SeriesDomain].
         */
        fun from(model: SeriesDomain) = MutableSeriesModel(
            model.userId,
            model.canonicalTitle,
            model.progress,
            model.totalLength,
            if (model.lengthKnown) model.totalLength.toString() else "-",
            model.type.name,
            model.subtype.name,
            model.userSeriesStatus,
            model.seriesStatus.name,
            model.rating.toFloat() / 2
        )
    }
}
