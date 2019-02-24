package com.chesire.malime.kitsu.api.search

import com.chesire.malime.core.Resource
import com.chesire.malime.core.models.SeriesModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response

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
        }
        val mockService = mockk<KitsuSearchService> {
            every {
                searchForAnimeAsync(any())
            } returns async {
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
            }
            val mockService = mockk<KitsuSearchService> {
                every {
                    searchForAnimeAsync(any())
                } returns async {
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
            every {
                searchForAnimeAsync(any())
            } returns async {
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
            every {
                searchForAnimeAsync(any())
            } returns async {
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
    fun `searchForManga failure response returns Resource#Error with errorBody`() = runBlocking {
        val expected = "errorBodyString"

        val mockResponseBody = mockk<ResponseBody> {
            every { string() } returns expected
        }
        val mockResponse = mockk<Response<List<SeriesModel>>> {
            every { isSuccessful } returns false
            every { errorBody() } returns mockResponseBody
        }
        val mockService = mockk<KitsuSearchService> {
            every {
                searchForMangaAsync(any())
            } returns async {
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
            }
            val mockService = mockk<KitsuSearchService> {
                every {
                    searchForMangaAsync(any())
                } returns async {
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
            every {
                searchForMangaAsync(any())
            } returns async {
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
            every {
                searchForMangaAsync(any())
            } returns async {
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
}
