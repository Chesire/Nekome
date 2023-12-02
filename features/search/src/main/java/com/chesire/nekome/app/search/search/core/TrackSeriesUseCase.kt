package com.chesire.nekome.app.search.search.core

import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.preferences.ApplicationPreferences
import com.chesire.nekome.datasource.series.SeriesRepository
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapEither
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class TrackSeriesUseCase @Inject constructor(
    private val seriesRepo: SeriesRepository,
    private val settings: ApplicationPreferences
) {

    suspend operator fun invoke(
        seriesId: Int,
        seriesType: SeriesType
    ): Result<Unit, Unit> {
        return withContext(Dispatchers.IO) {
            when (seriesType) {
                SeriesType.Anime -> seriesRepo.addAnime(
                    seriesId,
                    settings.defaultSeriesState.first()
                )

                SeriesType.Manga -> seriesRepo.addManga(
                    seriesId,
                    settings.defaultSeriesState.first()
                )

                else -> error("Unknown SeriesType: $seriesType")
            }
        }.mapEither(
            success = { },
            failure = { }
        )
    }
}
