package com.chesire.nekome.kitsu.search

import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.kitsu.search.dto.SearchResponseDto
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

class KitsuSearchTests {

    private val map = SearchItemDtoMapper()

    @Test
    fun `searchForAnime failure response returns Resource#Error with errorBody`() = runBlocking {
        val expected = "errorBodyString"

        val mockResponseBody = mockk<ResponseBody> {
            every { string() } returns expected
        }
        val mockResponse = mockk<Response<SearchResponseDto>> {
            every { isSuccessful } returns false
            every { errorBody() } returns mockResponseBody
            every { code() } returns 0
        }
        val mockService = mockk<KitsuSearchService> {
            coEvery {
                searchForAnimeAsync(any())
            } coAnswers {
                mockResponse
            }
        }

        val classUnderTest = KitsuSearch(mockService, map)
        val actual = classUnderTest.searchForAnime("Anime").getError()

        assertEquals(expected, actual?.message)
    }

    @Test
    fun `searchForAnime failure response returns Resource#Error with message if no error`() =
        runBlocking {
            val expected = "responseBodyString"

            val mockResponse = mockk<Response<SearchResponseDto>> {
                every { isSuccessful } returns false
                every { errorBody() } returns null
                every { message() } returns expected
                every { code() } returns 0
            }
            val mockService = mockk<KitsuSearchService> {
                coEvery {
                    searchForAnimeAsync(any())
                } coAnswers {
                    mockResponse
                }
            }

            val classUnderTest = KitsuSearch(mockService, map)
            val actual = classUnderTest.searchForAnime("Anime").getError()

            assertEquals(expected, actual?.message)
        }

    @Test
    fun `searchForAnime successful response with no body returns Resource#Error`() = runBlocking {
        val expected = "Response body is null"

        val mockResponse = mockk<Response<SearchResponseDto>> {
            every { isSuccessful } returns true
            every { body() } returns null
            every { message() } returns expected
        }
        val mockService = mockk<KitsuSearchService> {
            coEvery {
                searchForAnimeAsync(any())
            } coAnswers {
                mockResponse
            }
        }

        val classUnderTest = KitsuSearch(mockService, map)
        val actual = classUnderTest.searchForAnime("Anime").getError()

        assertEquals(expected, actual?.message)
    }

    @Test
    fun `searchForAnime successful response with body returns Resource#Success`() = runBlocking {
        val expected = SearchResponseDto(listOf(createSearchItemDto(SeriesType.Anime)))

        val mockResponse = mockk<Response<SearchResponseDto>> {
            every { isSuccessful } returns true
            every { body() } returns expected
        }
        val mockService = mockk<KitsuSearchService> {
            coEvery {
                searchForAnimeAsync(any())
            } coAnswers {
                mockResponse
            }
        }

        val classUnderTest = KitsuSearch(mockService, map)
        val result = classUnderTest.searchForAnime("Anime").get()

        assertNotNull(result)
    }

    @Test
    fun `searchForAnime on thrown exception return Resource#Error`() = runBlocking {
        val mockService = mockk<KitsuSearchService> {
            coEvery { searchForAnimeAsync(any()) } throws UnknownHostException()
        }

        val classUnderTest = KitsuSearch(mockService, map)
        val result = classUnderTest.searchForAnime("").getError()

        assertNotNull(result)
    }

    @Test
    fun `searchForManga failure response returns Resource#Error with errorBody`() = runBlocking {
        val expected = "errorBodyString"

        val mockResponseBody = mockk<ResponseBody> {
            every { string() } returns expected
        }
        val mockResponse = mockk<Response<SearchResponseDto>> {
            every { isSuccessful } returns false
            every { errorBody() } returns mockResponseBody
            every { code() } returns 0
        }
        val mockService = mockk<KitsuSearchService> {
            coEvery {
                searchForMangaAsync(any())
            } coAnswers {
                mockResponse
            }
        }

        val classUnderTest = KitsuSearch(mockService, map)
        val actual = classUnderTest.searchForManga("Manga").getError()

        assertEquals(expected, actual?.message)
    }

    @Test
    fun `searchForManga failure response returns Resource#Error with message if no error`() =
        runBlocking {
            val expected = "responseBodyString"

            val mockResponse = mockk<Response<SearchResponseDto>> {
                every { isSuccessful } returns false
                every { errorBody() } returns null
                every { message() } returns expected
                every { code() } returns 0
            }
            val mockService = mockk<KitsuSearchService> {
                coEvery {
                    searchForMangaAsync(any())
                } coAnswers {
                    mockResponse
                }
            }

            val classUnderTest = KitsuSearch(mockService, map)
            val actual = classUnderTest.searchForManga("Manga").getError()

            assertEquals(expected, actual?.message)
        }

    @Test
    fun `searchForManga successful response with no body returns Resource#Error`() = runBlocking {
        val expected = "Response body is null"

        val mockResponse = mockk<Response<SearchResponseDto>> {
            every { isSuccessful } returns true
            every { body() } returns null
            every { message() } returns expected
        }
        val mockService = mockk<KitsuSearchService> {
            coEvery {
                searchForMangaAsync(any())
            } coAnswers {
                mockResponse
            }
        }

        val classUnderTest = KitsuSearch(mockService, map)
        val actual = classUnderTest.searchForManga("Manga").getError()

        assertEquals(expected, actual?.message)
    }

    @Test
    fun `searchForManga successful response with body returns Resource#Success`() = runBlocking {
        val expected = SearchResponseDto(listOf(createSearchItemDto(SeriesType.Manga)))

        val mockResponse = mockk<Response<SearchResponseDto>> {
            every { isSuccessful } returns true
            every { body() } returns expected
        }
        val mockService = mockk<KitsuSearchService> {
            coEvery {
                searchForMangaAsync(any())
            } coAnswers {
                mockResponse
            }
        }

        val classUnderTest = KitsuSearch(mockService, map)
        val result = classUnderTest.searchForManga("Manga").get()

        assertNotNull(result)
    }

    @Test
    fun `searchForManga on thrown exception return Resource#Error`() = runBlocking {
        val mockService = mockk<KitsuSearchService> {
            coEvery { searchForMangaAsync(any()) } throws UnknownHostException()
        }

        val classUnderTest = KitsuSearch(mockService, map)
        val result = classUnderTest.searchForManga("").getError()

        assertNotNull(result)
    }
}
