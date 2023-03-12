@file:OptIn(ExperimentalCoroutinesApi::class)

package com.chesire.nekome.app.search.results.core

import com.chesire.nekome.core.Resource
import com.chesire.nekome.core.flags.SeriesStatus
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.core.preferences.ApplicationPreferences
import com.chesire.nekome.datasource.series.SeriesDomain
import com.chesire.nekome.datasource.series.SeriesRepository
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class TrackSeriesUseCaseTest {

    private val seriesRepo = mockk<SeriesRepository>()
    private val settings = mockk<ApplicationPreferences>()
    private lateinit var retrieveUserSeriesIds: TrackSeriesUseCase

    @Before
    fun setup() {
        clearAllMocks()

        every { settings.defaultSeriesState } returns UserSeriesStatus.Current
        retrieveUserSeriesIds = TrackSeriesUseCase(seriesRepo, settings)
    }

    @Test
    fun `invoke with anime series type, calls to add anime`() = runTest {
        val id = 15
        coEvery { seriesRepo.addAnime(id, UserSeriesStatus.Current) } returns Resource.Error("")

        retrieveUserSeriesIds(id, SeriesType.Anime)

        coVerify { seriesRepo.addAnime(id, UserSeriesStatus.Current) }
    }

    @Test
    fun `invoke with manga series type, calls to add manga`() = runTest {
        val id = 15
        coEvery { seriesRepo.addManga(id, UserSeriesStatus.Current) } returns Resource.Error("")

        retrieveUserSeriesIds(id, SeriesType.Manga)

        coVerify { seriesRepo.addManga(id, UserSeriesStatus.Current) }
    }

    @Test
    fun `on failure to add series, Err is returned`() = runTest {
        val id = 15
        coEvery { seriesRepo.addAnime(id, UserSeriesStatus.Current) } returns Resource.Error("")

        val result = retrieveUserSeriesIds(id, SeriesType.Anime)

        assertTrue(result is Err)
    }

    @Test
    fun `on success to add series, Ok is returned`() = runTest {
        val id = 15
        coEvery { seriesRepo.addAnime(id, UserSeriesStatus.Current) } returns Resource.Success(
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
            )
        )

        val result = retrieveUserSeriesIds(id, SeriesType.Anime)

        assertTrue(result is Ok)
    }
}
