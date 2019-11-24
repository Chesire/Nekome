package com.chesire.nekome.kitsu.api.trending

import com.chesire.nekome.core.models.SeriesModel
import com.chesire.nekome.server.Resource
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import java.net.UnknownHostException

class KitsuTrendingTests {
    @Test
    fun `trendingAnime failure response returns Resource#Error with errorBody`() = runBlocking {
        val expected = "errorBodyString"

        val mockResponseBody = mockk<ResponseBody> {
            every { string() } returns expected
        }
        val mockResponse = mockk<Response<List<SeriesModel>>> {
            every { isSuccessful } returns false
            every { errorBody() } returns mockResponseBody
            every { code() } returns 0
        }
        val mockService = mockk<KitsuTrendingService> {
            coEvery {
                getTrendingAnimeAsync()
            } coAnswers {
                mockResponse
            }
        }

        val classUnderTest = KitsuTrending(mockService)

        when (val actual = classUnderTest.trendingAnime()) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> Assert.assertEquals(expected, actual.msg)
        }
    }

    @Test
    fun `trendingAnime failure response returns Resource#Error with message if no error`() =
        runBlocking {
            val expected = "responseBodyString"

            val mockResponse = mockk<Response<List<SeriesModel>>> {
                every { isSuccessful } returns false
                every { errorBody() } returns null
                every { message() } returns expected
                every { code() } returns 0
            }
            val mockService = mockk<KitsuTrendingService> {
                coEvery {
                    getTrendingAnimeAsync()
                } coAnswers {
                    mockResponse
                }
            }

            val classUnderTest = KitsuTrending(mockService)

            when (val actual = classUnderTest.trendingAnime()) {
                is Resource.Success -> error("Test has failed")
                is Resource.Error -> Assert.assertEquals(expected, actual.msg)
            }
        }

    @Test
    fun `trendingAnime successful response with no body returns Resource#Error`() = runBlocking {
        val expected = "Response body is null"

        val mockResponse = mockk<Response<List<SeriesModel>>> {
            every { isSuccessful } returns true
            every { body() } returns null
            every { message() } returns expected
        }
        val mockService = mockk<KitsuTrendingService> {
            coEvery {
                getTrendingAnimeAsync()
            } coAnswers {
                mockResponse
            }
        }

        val classUnderTest = KitsuTrending(mockService)

        when (val actual = classUnderTest.trendingAnime()) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> Assert.assertEquals(expected, actual.msg)
        }
    }

    @Test
    fun `trendingAnime successful response with body returns Resource#Success`() = runBlocking {
        val expected = listOf<SeriesModel>(mockk())

        val mockResponse = mockk<Response<List<SeriesModel>>> {
            every { isSuccessful } returns true
            every { body() } returns expected
        }
        val mockService = mockk<KitsuTrendingService> {
            coEvery {
                getTrendingAnimeAsync()
            } coAnswers {
                mockResponse
            }
        }

        val classUnderTest = KitsuTrending(mockService)

        when (val actual = classUnderTest.trendingAnime()) {
            is Resource.Success -> Assert.assertEquals(expected, actual.data)
            is Resource.Error -> error("Test has failed")
        }
    }

    @Test
    fun `trendingAnime on thrown exception return Resource#Error`() = runBlocking {
        val mockService = mockk<KitsuTrendingService> {
            coEvery { getTrendingAnimeAsync() } throws UnknownHostException()
        }

        val classUnderTest = KitsuTrending(mockService)

        when (classUnderTest.trendingAnime()) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> Assert.assertTrue(true)
        }
    }

    @Test
    fun `trendingManga failure response returns Resource#Error with errorBody`() = runBlocking {
        val expected = "errorBodyString"

        val mockResponseBody = mockk<ResponseBody> {
            every { string() } returns expected
        }
        val mockResponse = mockk<Response<List<SeriesModel>>> {
            every { isSuccessful } returns false
            every { errorBody() } returns mockResponseBody
            every { code() } returns 0
        }
        val mockService = mockk<KitsuTrendingService> {
            coEvery {
                getTrendingMangaAsync()
            } coAnswers {
                mockResponse
            }
        }

        val classUnderTest = KitsuTrending(mockService)

        when (val actual = classUnderTest.trendingManga()) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> Assert.assertEquals(expected, actual.msg)
        }
    }

    @Test
    fun `trendingManga failure response returns Resource#Error with message if no error`() =
        runBlocking {
            val expected = "responseBodyString"

            val mockResponse = mockk<Response<List<SeriesModel>>> {
                every { isSuccessful } returns false
                every { errorBody() } returns null
                every { message() } returns expected
                every { code() } returns 0
            }
            val mockService = mockk<KitsuTrendingService> {
                coEvery {
                    getTrendingMangaAsync()
                } coAnswers {
                    mockResponse
                }
            }

            val classUnderTest = KitsuTrending(mockService)

            when (val actual = classUnderTest.trendingManga()) {
                is Resource.Success -> error("Test has failed")
                is Resource.Error -> Assert.assertEquals(expected, actual.msg)
            }
        }

    @Test
    fun `trendingManga successful response with no body returns Resource#Error`() = runBlocking {
        val expected = "Response body is null"

        val mockResponse = mockk<Response<List<SeriesModel>>> {
            every { isSuccessful } returns true
            every { body() } returns null
            every { message() } returns expected
        }
        val mockService = mockk<KitsuTrendingService> {
            coEvery {
                getTrendingMangaAsync()
            } coAnswers {
                mockResponse
            }
        }

        val classUnderTest = KitsuTrending(mockService)

        when (val actual = classUnderTest.trendingManga()) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> Assert.assertEquals(expected, actual.msg)
        }
    }

    @Test
    fun `trendingManga successful response with body returns Resource#Success`() = runBlocking {
        val expected = listOf<SeriesModel>(mockk())

        val mockResponse = mockk<Response<List<SeriesModel>>> {
            every { isSuccessful } returns true
            every { body() } returns expected
        }
        val mockService = mockk<KitsuTrendingService> {
            coEvery {
                getTrendingMangaAsync()
            } coAnswers {
                mockResponse
            }
        }

        val classUnderTest = KitsuTrending(mockService)

        when (val actual = classUnderTest.trendingManga()) {
            is Resource.Success -> Assert.assertEquals(expected, actual.data)
            is Resource.Error -> error("Test has failed")
        }
    }

    @Test
    fun `trendingManga on thrown exception return Resource#Error`() = runBlocking {
        val mockService = mockk<KitsuTrendingService> {
            coEvery { getTrendingMangaAsync() } throws UnknownHostException()
        }

        val classUnderTest = KitsuTrending(mockService)

        when (classUnderTest.trendingManga()) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> Assert.assertTrue(true)
        }
    }
}
