package com.chesire.nekome.app.series.collection.core

import com.chesire.nekome.core.Resource
import com.chesire.nekome.datasource.series.SeriesDomain
import com.chesire.nekome.datasource.series.SeriesRepository
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteSeriesUseCase @Inject constructor(private val repo: SeriesRepository) {

    // TODO: Maybe update this so it takes an ID instead?
    //  surely we can send just the id and do a WHERE clause for the DB? But might need whole domain
    //  lower down for the title in the err etc
    suspend operator fun invoke(domain: SeriesDomain): Result<Unit, Unit> {
        return withContext(Dispatchers.IO) {
            val result = repo.deleteSeries(domain)

            if (result is Resource.Success) {
                Ok(Unit)
            } else {
                Err(Unit)
            }
        }
    }
}
