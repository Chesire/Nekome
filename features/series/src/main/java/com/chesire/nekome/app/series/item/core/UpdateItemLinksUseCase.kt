package com.chesire.nekome.app.series.item.core

import com.chesire.nekome.datasource.series.SeriesRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UpdateItemLinksUseCase @Inject constructor(private val seriesRepo: SeriesRepository) {

    suspend operator fun invoke(
        userSeriesId: Int,
        linkId: String?,
        displayText: String,
        linkText: String
    ) {
        // TODO: Validate the displayText?
        // TODO: Validate the linkText?
        withContext(Dispatchers.IO) {
            seriesRepo.updateSeriesLinks(
                userSeriesId = userSeriesId,
                id = linkId,
                displayText = displayText,
                linkText = linkText
            )
        }
    }
}
