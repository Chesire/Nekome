package com.chesire.malime.kitsu.api.search

import com.chesire.malime.server.Resource
import com.chesire.malime.core.models.SeriesModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response
import java.net.UnknownHostException

class KitsuSearchTests {
    @Test
    fun `searchForAnime failure response returns Resource#Error with errorBody`() = runBlocking {
        val expected = "errorBodyString"

        val mockResponseBody = mockk<ResponseBody> {
            every { string() } returns expected
        }
        val mockResponse = mockk<Response<List<SeriesModel>>> {
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

        val classUnderTest = KitsuSearch(mockService)
        val actual = classUnderTest.searchForAnime("Anime")

        when (actual) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> assertEquals(expected, actual.msg)
        }
    }

    @Test
    fun `searchForAnime failure response returns Resource#Error with message if no error`() =
        runBlocking {
            val expected = "responseBodyString"

            val mockResponse = mockk<Response<List<SeriesModel>>> {
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

            val classUnderTest = KitsuSearch(mockService)
            val actual = classUnderTest.searchForAnime("Anime")

            when (actual) {
                is Resource.Success -> error("Test has failed")
                is Resource.Error -> assertEquals(expected, actual.msg)
            }
        }

    @Test
    fun `searchForAnime successful response with no body returns Resource#Error`() = runBlocking {
        val expected = "Response body is null"

        val mockResponse = mockk<Response<List<SeriesModel>>> {
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

        val classUnderTest = KitsuSearch(mockService)
        val actual = classUnderTest.searchForAnime("Anime")

        when (actual) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> assertEquals(expected, actual.msg)
        }
    }

    @Test
    fun `searchForAnime successful response with body returns Resource#Success`() = runBlocking {
        val expected = listOf<SeriesModel>(mockk())

        val mockResponse = mockk<Response<List<SeriesModel>>> {
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

        val classUnderTest = KitsuSearch(mockService)
        val actual = classUnderTest.searchForAnime("Anime")

        when (actual) {
            is Resource.Success -> assertEquals(expected, actual.data)
            is Resource.Error -> error("Test has failed")
        }
    }

    @Test
    fun `searchForAnime on thrown exception return Resource#Error`() = runBlocking {
        val mockService = mockk<KitsuSearchService> {
            coEvery { searchForAnimeAsync(any()) } throws UnknownHostException()
        }

        val classUnderTest = KitsuSearch(mockService)
        val result = classUnderTest.searchForAnime("")

        when (result) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> assertTrue(true)
        }
    }

    @Test
    fun `searchForManga failure response returns Resource#Error with errorBody`() = runBlocking {
        val expected = "errorBodyString"

        val mockResponseBody = mockk<ResponseBody> {
            every { string() } returns expected
        }
        val mockResponse = mockk<Response<List<SeriesModel>>> {
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

        val classUnderTest = KitsuSearch(mockService)
        val actual = classUnderTest.searchForManga("Manga")

        when (actual) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> assertEquals(expected, actual.msg)
        }
    }

    @Test
    fun `searchForManga failure response returns Resource#Error with message if no error`() =
        runBlocking {
            val expected = "responseBodyString"

            val mockResponse = mockk<Response<List<SeriesModel>>> {
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

            val classUnderTest = KitsuSearch(mockService)
            val actual = classUnderTest.searchForManga("Manga")

            when (actual) {
                is Resource.Success -> error("Test has failed")
                is Resource.Error -> assertEquals(expected, actual.msg)
            }
        }

    @Test
    fun `searchForManga successful response with no body returns Resource#Error`() = runBlocking {
        val expected = "Response body is null"

        val mockResponse = mockk<Response<List<SeriesModel>>> {
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

        val classUnderTest = KitsuSearch(mockService)
        val actual = classUnderTest.searchForManga("Manga")

        when (actual) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> assertEquals(expected, actual.msg)
        }
    }

    @Test
    fun `searchForManga successful response with body returns Resource#Success`() = runBlocking {
        val expected = listOf<SeriesModel>(mockk())

        val mockResponse = mockk<Response<List<SeriesModel>>> {
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

        val classUnderTest = KitsuSearch(mockService)
        val actual = classUnderTest.searchForManga("Manga")

        when (actual) {
            is Resource.Success -> assertEquals(expected, actual.data)
            is Resource.Error -> error("Test has failed")
        }
    }

    @Test
    fun `searchForManga on thrown exception return Resource#Error`() = runBlocking {
        val mockService = mockk<KitsuSearchService> {
            coEvery { searchForMangaAsync(any()) } throws UnknownHostException()
        }

        val classUnderTest = KitsuSearch(mockService)
        val result = classUnderTest.searchForManga("")

        when (result) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> assertTrue(true)
        }
    }
}
