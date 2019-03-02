package com.chesire.malime.kitsu.api.library

import com.chesire.malime.core.Resource
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.kitsu.api.intermediaries.Links
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class KitsuLibraryTests {
    @Test
    fun `retrieveAnime on success returns the retrieved models`() = runBlocking {
        val expected = listOf(mockk<SeriesModel>())
        val links = Links()
        val mockService = mockk<KitsuLibraryService> {
            every {
                retrieveAnimeAsync(1, 0, 500)
            } returns async {
                mockk<Response<ParsedRetrieveResponse>> {
                    every { isSuccessful } returns true
                    every { body() } returns ParsedRetrieveResponse(expected, links)
                }
            }
        }
        val classUnderTest = KitsuLibrary(mockService, 1)
        val actual = classUnderTest.retrieveAnime()

        verify(exactly = 1) { mockService.retrieveAnimeAsync(1, 0, 500) }
        when (actual) {
            is Resource.Success -> assertEquals(expected, actual.data)
            is Resource.Error -> error("Test has failed")
        }
    }

    @Test
    fun `retrieveAnime more items to retrieve executes again with new offset`() = runBlocking {
        val expected = listOf(mockk<SeriesModel>())
        val mockService = mockk<KitsuLibraryService> {
            every {
                retrieveAnimeAsync(1, 0, 500)
            } returns async {
                mockk<Response<ParsedRetrieveResponse>> {
                    every { isSuccessful } returns true
                    every { body() } returns ParsedRetrieveResponse(expected, Links(next = "next"))
                }
            }
            every {
                retrieveAnimeAsync(1, 500, 500)
            } returns async {
                mockk<Response<ParsedRetrieveResponse>> {
                    every { isSuccessful } returns true
                    every { body() } returns ParsedRetrieveResponse(expected, Links())
                }
            }
        }
        val classUnderTest = KitsuLibrary(mockService, 1)
        classUnderTest.retrieveAnime()

        verify(exactly = 2) { mockService.retrieveAnimeAsync(any(), any(), any()) }
    }

    @Test
    fun `retrieveAnime on error retries up to 4 times`() = runBlocking {
        val mockService = mockk<KitsuLibraryService> {
            every {
                retrieveAnimeAsync(1, 0, 500)
            } returns async {
                mockk<Response<ParsedRetrieveResponse>> {
                    every { isSuccessful } returns false
                    every { code() } returns 0
                    every { errorBody() } returns mockk {
                        every { string() } returns ""
                    }
                }
            }
        }
        val classUnderTest = KitsuLibrary(mockService, 1)
        val actual = classUnderTest.retrieveAnime()

        verify(exactly = 4) { mockService.retrieveAnimeAsync(1, 0, 500) }
        when (actual) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> assertTrue(true)
        }
    }

    @Test
    fun `retrieveAnime error is produced only if no models are found`() = runBlocking {
        val mockService = mockk<KitsuLibraryService> {
            every {
                retrieveAnimeAsync(1, 0, 500)
            } returns async {
                mockk<Response<ParsedRetrieveResponse>> {
                    every { isSuccessful } returns true
                    every { body() } returns ParsedRetrieveResponse(
                        listOf(mockk()),
                        Links(next = "next")
                    )
                }
            }
            every {
                retrieveAnimeAsync(1, 500, 500)
            } returns async {
                mockk<Response<ParsedRetrieveResponse>> {
                    every { isSuccessful } returns false
                    every { code() } returns 0
                    every { errorBody() } returns mockk {
                        every { string() } returns ""
                    }
                }
            }
        }
        val classUnderTest = KitsuLibrary(mockService, 1)
        val actual = classUnderTest.retrieveAnime()

        verify { mockService.retrieveAnimeAsync(1, 0, 500) }
        verify(exactly = 4) { mockService.retrieveAnimeAsync(1, 500, 500) }
        when (actual) {
            is Resource.Success -> assertTrue(true)
            is Resource.Error -> error("Test has failed")
        }
    }

    @Test
    fun `retrieveManga on success returns the retrieved models`() = runBlocking {
        val expected = listOf(mockk<SeriesModel>())
        val mockService = mockk<KitsuLibraryService> {
            every {
                retrieveMangaAsync(1, 0, 500)
            } returns async {
                mockk<Response<ParsedRetrieveResponse>> {
                    every { isSuccessful } returns true
                    every { body() } returns ParsedRetrieveResponse(expected, Links())
                }
            }
        }
        val classUnderTest = KitsuLibrary(mockService, 1)
        val actual = classUnderTest.retrieveManga()

        when (actual) {
            is Resource.Success -> assertEquals(expected, actual.data)
            is Resource.Error -> error("Test has failed")
        }
    }

    @Test
    fun `retrieveManga more items to retrieve executes again with new offset`() = runBlocking {
        val expected = listOf(mockk<SeriesModel>())
        val mockService = mockk<KitsuLibraryService> {
            every {
                retrieveMangaAsync(1, 0, 500)
            } returns async {
                mockk<Response<ParsedRetrieveResponse>> {
                    every { isSuccessful } returns true
                    every { body() } returns ParsedRetrieveResponse(expected, Links(next = "next"))
                }
            }
            every {
                retrieveMangaAsync(1, 500, 500)
            } returns async {
                mockk<Response<ParsedRetrieveResponse>> {
                    every { isSuccessful } returns true
                    every { body() } returns ParsedRetrieveResponse(expected, Links())
                }
            }
        }
        val classUnderTest = KitsuLibrary(mockService, 1)
        classUnderTest.retrieveManga()

        verify(exactly = 2) { mockService.retrieveMangaAsync(any(), any(), any()) }
    }

    @Test
    fun `retrieveManga on error retries up to 4 times`() = runBlocking {
        val mockService = mockk<KitsuLibraryService> {
            every {
                retrieveMangaAsync(1, 0, 500)
            } returns async {
                mockk<Response<ParsedRetrieveResponse>> {
                    every { isSuccessful } returns false
                    every { code() } returns 0
                    every { errorBody() } returns mockk {
                        every { string() } returns ""
                    }
                }
            }
        }
        val classUnderTest = KitsuLibrary(mockService, 1)
        val actual = classUnderTest.retrieveManga()

        verify(exactly = 4) { mockService.retrieveMangaAsync(1, 0, 500) }
        when (actual) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> assertTrue(true)
        }
    }

    @Test
    fun `retrieveManga error is produced only if no models are found`() = runBlocking {
        val mockService = mockk<KitsuLibraryService> {
            every {
                retrieveMangaAsync(1, 0, 500)
            } returns async {
                mockk<Response<ParsedRetrieveResponse>> {
                    every { isSuccessful } returns true
                    every { body() } returns ParsedRetrieveResponse(
                        listOf(mockk()),
                        Links(next = "next")
                    )
                }
            }
            every {
                retrieveMangaAsync(1, 500, 500)
            } returns async {
                mockk<Response<ParsedRetrieveResponse>> {
                    every { isSuccessful } returns false
                    every { code() } returns 0
                    every { errorBody() } returns mockk {
                        every { string() } returns ""
                    }
                }
            }
        }
        val classUnderTest = KitsuLibrary(mockService, 1)
        val actual = classUnderTest.retrieveManga()

        verify { mockService.retrieveMangaAsync(1, 0, 500) }
        verify(exactly = 4) { mockService.retrieveMangaAsync(1, 500, 500) }
        when (actual) {
            is Resource.Success -> assertTrue(true)
            is Resource.Error -> error("Test has failed")
        }
    }

    @Test
    fun `addAnime returns the added SeriesModel`() = runBlocking {
        val mockModel = mockk<SeriesModel>()
        val mockResponse = mockk<Response<SeriesModel>> {
            every { isSuccessful } returns true
            every { body() } returns mockModel
        }
        val mockService = mockk<KitsuLibraryService> {
            every {
                addAnimeAsync(any())
            } returns async {
                mockResponse
            }
        }

        val classUnderTest = KitsuLibrary(mockService, 1)
        val actual = classUnderTest.addAnime(10, UserSeriesStatus.Planned)

        when (actual) {
            is Resource.Success -> assertSame(mockModel, actual.data)
            is Resource.Error -> error("Test has failed")
        }
    }

    @Test
    fun `addManga returns the added SeriesModel`() = runBlocking {
        val mockModel = mockk<SeriesModel>()
        val mockResponse = mockk<Response<SeriesModel>> {
            every { isSuccessful } returns true
            every { body() } returns mockModel
        }
        val mockService = mockk<KitsuLibraryService> {
            every {
                addMangaAsync(any())
            } returns async {
                mockResponse
            }
        }

        val classUnderTest = KitsuLibrary(mockService, 1)
        val actual = classUnderTest.addManga(10, UserSeriesStatus.Planned)

        when (actual) {
            is Resource.Success -> assertSame(mockModel, actual.data)
            is Resource.Error -> error("Test has failed")
        }
    }

    @Test
    fun `update success returns Resource#Success with SeriesModel`() = runBlocking {
        val mockModel = mockk<SeriesModel>()
        val mockResponse = mockk<Response<SeriesModel>> {
            every { isSuccessful } returns true
            every { body() } returns mockModel
        }
        val mockService = mockk<KitsuLibraryService> {
            every {
                updateItemAsync(10, any())
            } returns async {
                mockResponse
            }
        }

        val classUnderTest = KitsuLibrary(mockService, 1)
        val actual = classUnderTest.update(10, 0, UserSeriesStatus.OnHold)

        when (actual) {
            is Resource.Success -> assertSame(mockModel, actual.data)
            is Resource.Error -> error("Test has failed")
        }
    }

    @Test
    fun `update success returns Resource#Error if no body`() = runBlocking {
        val expected = "Response body is null"
        val mockResponse = mockk<Response<SeriesModel>> {
            every { isSuccessful } returns true
            every { body() } returns null
        }
        val mockService = mockk<KitsuLibraryService> {
            every {
                updateItemAsync(10, any())
            } returns async {
                mockResponse
            }
        }

        val classUnderTest = KitsuLibrary(mockService, 1)
        val actual = classUnderTest.update(10, 0, UserSeriesStatus.OnHold)

        when (actual) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> assertSame(expected, actual.msg)
        }
    }

    @Test
    fun `update failure returns Resource#Error with errorBody`() = runBlocking {
        val expected = "errorBodyString"

        val mockResponseBody = mockk<ResponseBody> {
            every { string() } returns expected
        }
        val mockResponse = mockk<Response<SeriesModel>> {
            every { isSuccessful } returns false
            every { errorBody() } returns mockResponseBody
            every { code() } returns 0
        }
        val mockService = mockk<KitsuLibraryService> {
            every {
                updateItemAsync(10, any())
            } returns async {
                mockResponse
            }
        }

        val classUnderTest = KitsuLibrary(mockService, 1)
        val actual = classUnderTest.update(10, 0, UserSeriesStatus.OnHold)

        when (actual) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> assertSame(expected, actual.msg)
        }
    }

    @Test
    fun `update failure returns Resource#Error with message if null errorBody`() = runBlocking {
        val expected = "errorBodyString"

        val mockResponse = mockk<Response<SeriesModel>> {
            every { isSuccessful } returns false
            every { errorBody() } returns null
            every { message() } returns expected
            every { code() } returns 0
        }
        val mockService = mockk<KitsuLibraryService> {
            every {
                updateItemAsync(10, any())
            } returns async {
                mockResponse
            }
        }

        val classUnderTest = KitsuLibrary(mockService, 1)
        val actual = classUnderTest.update(10, 0, UserSeriesStatus.OnHold)

        when (actual) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> assertSame(expected, actual.msg)
        }
    }

    @Test
    fun `delete success returns Resource#Success`() = runBlocking {
        val mockResponse = mockk<Response<Any>> {
            every { isSuccessful } returns true
        }
        val mockService = mockk<KitsuLibraryService> {
            every {
                deleteItemAsync(10)
            } returns async {
                mockResponse
            }
        }

        val classUnderTest = KitsuLibrary(mockService, 1)
        val actual = classUnderTest.delete(10)

        when (actual) {
            is Resource.Success -> assertTrue(true)
            is Resource.Error -> error("Test has failed")
        }
    }

    @Test
    fun `delete failure returns Resource#Error with errorBody`() = runBlocking {
        val expected = "errorBodyString"

        val mockResponseBody = mockk<ResponseBody> {
            every { string() } returns expected
        }
        val mockResponse = mockk<Response<Any>> {
            every { isSuccessful } returns false
            every { errorBody() } returns mockResponseBody
            every { code() } returns 0
        }
        val mockService = mockk<KitsuLibraryService> {
            every {
                deleteItemAsync(10)
            } returns async {
                mockResponse
            }
        }

        val classUnderTest = KitsuLibrary(mockService, 1)
        val actual = classUnderTest.delete(10)

        when (actual) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> assertEquals(expected, actual.msg)
        }
    }

    @Test
    fun `delete failure returns Resource#Error with message if null errorBody`() = runBlocking {
        val expected = "errorBodyString"

        val mockResponse = mockk<Response<Any>> {
            every { isSuccessful } returns false
            every { errorBody() } returns null
            every { message() } returns expected
            every { code() } returns 0
        }
        val mockService = mockk<KitsuLibraryService> {
            every {
                deleteItemAsync(10)
            } returns async {
                mockResponse
            }
        }

        val classUnderTest = KitsuLibrary(mockService, 1)
        val actual = classUnderTest.delete(10)

        when (actual) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> assertEquals(expected, actual.msg)
        }
    }
}
