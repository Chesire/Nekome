package com.chesire.malime.flow.series.detail

import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.SeriesModel

/**
 * Provides an object based on [SeriesModel] that allows modifying values for series detail.
 */
data class MutableSeriesModel(
    val seriesName: String,
    var seriesProgress: Int,
    val seriesLength: String,
    val seriesType: String,
    val seriesSubType: String,
    var userSeriesStatus: UserSeriesStatus
) {
    companion object {
        /**
         * Creates an instance of [MutableSeriesModel] from an instance of [SeriesModel].
         */
        fun from(model: SeriesModel): MutableSeriesModel {
            return MutableSeriesModel(
                model.title,
                model.progress,
                if (model.lengthKnown) model.totalLength.toString() else "??",
                model.type.name,
                model.subtype.name,
                model.userSeriesStatus
            )
        }
    }
}
