package com.chesire.nekome.kitsu.trending

import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.kitsu.trending.dto.TrendingResponseDto
import com.github.michaelbull.result.get
import com.github.michaelbull.result.getError
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import java.net.UnknownHostException
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import retrofit2.Response

class KitsuTrendingTests {
    private val map = TrendingItemDtoMapper()

    @Test
    fun `trendingAnime failure response returns Err with errorBody`() = runBlocking {
        val expected = "errorBodyString"

        val mockResponseBody = mockk<ResponseBody> {
            every { string() } returns expected
        }
        val mockResponse = mockk<Response<TrendingResponseDto>> {
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

        val classUnderTest = KitsuTrending(mockService, map)
        val actual = classUnderTest.getTrendingAnime().getError()

        assertEquals(expected, actual?.message)
    }

    @Test
    fun `trendingAnime failure response returns Err with message if no error`() =
        runBlocking {
            val expected = "responseBodyString"

            val mockResponse = mockk<Response<TrendingResponseDto>> {
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

            val classUnderTest = KitsuTrending(mockService, map)
            val actual = classUnderTest.getTrendingAnime().getError()

            assertEquals(expected, actual?.message)
        }

    @Test
    fun `trendingAnime successful response with no body returns Err`() = runBlocking {
        val expected = "Response body is null"

        val mockResponse = mockk<Response<TrendingResponseDto>> {
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

        val classUnderTest = KitsuTrending(mockService, map)
        val actual = classUnderTest.getTrendingAnime().getError()

        assertEquals(expected, actual?.message)
    }

    @Test
    fun `trendingAnime successful response with body returns Ok`() = runBlocking {
        val expected = TrendingResponseDto(listOf(createTrendingItemDto(SeriesType.Anime)))

        val mockResponse = mockk<Response<TrendingResponseDto>> {
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

        val classUnderTest = KitsuTrending(mockService, map)
        val result = classUnderTest.getTrendingAnime().get()

        assertNotNull(result)
    }

    @Test
    fun `trendingAnime on thrown exception return Err`() = runBlocking {
        val mockService = mockk<KitsuTrendingService> {
            coEvery { getTrendingAnimeAsync() } throws UnknownHostException()
        }

        val classUnderTest = KitsuTrending(mockService, map)
        val result = classUnderTest.getTrendingAnime().getError()

        assertNotNull(result)
    }

    @Test
    fun `trendingManga failure response returns Err with errorBody`() = runBlocking {
        val expected = "errorBodyString"

        val mockResponseBody = mockk<ResponseBody> {
            every { string() } returns expected
        }
        val mockResponse = mockk<Response<TrendingResponseDto>> {
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

        val classUnderTest = KitsuTrending(mockService, map)
        val actual = classUnderTest.getTrendingManga().getError()

        assertEquals(expected, actual?.message)
    }

    @Test
    fun `trendingManga failure response returns Err with message if no error`() =
        runBlocking {
            val expected = "responseBodyString"

            val mockResponse = mockk<Response<TrendingResponseDto>> {
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

            val classUnderTest = KitsuTrending(mockService, map)
            val actual = classUnderTest.getTrendingManga().getError()

            assertEquals(expected, actual?.message)
        }

    @Test
    fun `trendingManga successful response with no body returns Err`() = runBlocking {
        val expected = "Response body is null"

        val mockResponse = mockk<Response<TrendingResponseDto>> {
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

        val classUnderTest = KitsuTrending(mockService, map)
        val actual = classUnderTest.getTrendingManga().getError()

        assertEquals(expected, actual?.message)
    }

    @Test
    fun `trendingManga successful response with body returns Ok`() = runBlocking {
        val expected = TrendingResponseDto(listOf(createTrendingItemDto(SeriesType.Manga)))

        val mockResponse = mockk<Response<TrendingResponseDto>> {
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

        val classUnderTest = KitsuTrending(mockService, map)
        val result = classUnderTest.getTrendingManga().get()

        assertNotNull(result)
    }

    @Test
    fun `trendingManga on thrown exception return Err`() = runBlocking {
        val mockService = mockk<KitsuTrendingService> {
            coEvery { getTrendingMangaAsync() } throws UnknownHostException()
        }

        val classUnderTest = KitsuTrending(mockService, map)
        val result = classUnderTest.getTrendingManga().getError()

        assertNotNull(result)
    }
}
