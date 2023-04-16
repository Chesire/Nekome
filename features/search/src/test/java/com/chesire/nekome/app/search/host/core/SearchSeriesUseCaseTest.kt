@file:OptIn(ExperimentalCoroutinesApi::class)

package com.chesire.nekome.app.search.host.core

import com.chesire.nekome.app.search.host.core.model.SearchGroup
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.datasource.search.SearchDomain
import com.chesire.nekome.datasource.search.remote.SearchApi
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SearchSeriesUseCaseTest {

    private val searchApi = mockk<SearchApi>()
    private lateinit var searchSeries: SearchSeriesUseCase

    @Before
    fun setup() {
        clearAllMocks()

        searchSeries = SearchSeriesUseCase(searchApi)
    }

    @Test
    fun `Given invalid title, When invoke, Then InvalidTitle Err is returned`() = runTest {
        val expected = Err(SearchFailureReason.InvalidTitle)

        val result = searchSeries.invoke("", SearchGroup.Manga)

        assertEquals(expected, result)
    }

    @Test
    fun `Given Anime SearchGroup, When invoke, Then anime endpoint is hit`() = runTest {
        coEvery { searchApi.searchForAnime(any()) } returns Resource.Error("")

        searchSeries.invoke("title", SearchGroup.Anime)

        coVerify { searchApi.searchForAnime("title") }
    }

    @Test
    fun `Given Manga SearchGroup, When invoke, Then manga endpoint is hit`() = runTest {
        coEvery { searchApi.searchForManga(any()) } returns Resource.Error("")

        searchSeries.invoke("title", SearchGroup.Manga)

        coVerify { searchApi.searchForManga("title") }
    }

    @Test
    fun `Given api call failure, When invoke, Then NetworkError Err is returned`() = runTest {
        coEvery { searchApi.searchForManga(any()) } returns Resource.Error("")
        val expected = Err(SearchFailureReason.NetworkError)

        val result = searchSeries.invoke("title", SearchGroup.Manga)

        assertEquals(expected, result)
    }

    @Test
    fun `Given api call success, but no series, When invoke, Then NoSeriesFound Err is returned`() =
        runTest {
            coEvery { searchApi.searchForManga(any()) } returns Resource.Success(emptyList())
            val expected = Err(SearchFailureReason.NoSeriesFound)

            val result = searchSeries.invoke("title", SearchGroup.Manga)

            assertEquals(expected, result)
        }

    @Test
    fun `Given api call success, When invoke, Then mapped data is returned`() = runTest {
        coEvery {
            searchApi.searchForManga(any())
        } returns Resource.Success(
            listOf(
                SearchDomain(
                    id = 55,
                    type = SeriesType.Manga,
                    synopsis = "",
                    canonicalTitle = "",
                    subtype = Subtype.Manga,
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
                    )
                )
            )
        )
        val expected = Ok(
            listOf(
                SearchDomain(
                    id = 55,
                    type = SeriesType.Manga,
                    synopsis = "",
                    canonicalTitle = "",
                    subtype = Subtype.Manga,
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
                    )
                )
            )
        )

        val result = searchSeries.invoke("title", SearchGroup.Manga)

        assertEquals(expected, result)
    }
}
