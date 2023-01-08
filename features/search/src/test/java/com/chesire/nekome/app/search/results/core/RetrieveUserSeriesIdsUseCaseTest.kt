@file:OptIn(ExperimentalCoroutinesApi::class)

package com.chesire.nekome.app.search.results.core

import com.chesire.nekome.core.flags.SeriesStatus
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.datasource.series.SeriesDomain
import com.chesire.nekome.datasource.series.SeriesRepository
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RetrieveUserSeriesIdsUseCaseTest {

    private val seriesRepo = mockk<SeriesRepository>()
    private lateinit var retrieveUserSeriesIds: RetrieveUserSeriesIdsUseCase

    @Before
    fun setup() {
        clearAllMocks()

        retrieveUserSeriesIds = RetrieveUserSeriesIdsUseCase(seriesRepo)
    }

    @Test
    fun `invoke returns the latest flow of series ids`() = runTest {
        every {
            seriesRepo.getSeries()
        } returns flowOf(
            listOf(
                SeriesDomain(
                    id = 15,
                    userId = 0,
                    type = SeriesType.Anime,
                    subtype = Subtype.Movie,
                    slug = "",
                    title = "",
                    seriesStatus = SeriesStatus.Current,
                    userSeriesStatus = UserSeriesStatus.Current,
                    progress = 0,
                    totalLength = 0,
                    rating = 0,
                    posterImage = ImageModel(
                        tiny = ImageModel.ImageData(
                            url = "",
                            width = 0,
                            height = 0
                        ),
                        small = ImageModel.ImageData(
                            url = "",
                            width = 0,
                            height = 0
                        ),
                        medium = ImageModel.ImageData(
                            url = "",
                            width = 0,
                            height = 0
                        ),
                        large = ImageModel.ImageData(
                            url = "",
                            width = 0,
                            height = 0
                        )
                    ),
                    startDate = "",
                    endDate = ""
                ),
                SeriesDomain(
                    id = 18,
                    userId = 0,
                    type = SeriesType.Anime,
                    subtype = Subtype.Movie,
                    slug = "",
                    title = "",
                    seriesStatus = SeriesStatus.Current,
                    userSeriesStatus = UserSeriesStatus.Current,
                    progress = 0,
                    totalLength = 0,
                    rating = 0,
                    posterImage = ImageModel(
                        tiny = ImageModel.ImageData(
                            url = "",
                            width = 0,
                            height = 0
                        ),
                        small = ImageModel.ImageData(
                            url = "",
                            width = 0,
                            height = 0
                        ),
                        medium = ImageModel.ImageData(
                            url = "",
                            width = 0,
                            height = 0
                        ),
                        large = ImageModel.ImageData(
                            url = "",
                            width = 0,
                            height = 0
                        )
                    ),
                    startDate = "",
                    endDate = ""
                ),
            )
        )

        val result = retrieveUserSeriesIds()

        assertEquals(
            listOf(15, 18),
            result.first()
        )
    }
}
