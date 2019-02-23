package com.chesire.malime.kitsu.api.library

import com.chesire.malime.core.Resource
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.SeriesModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Ignore
import org.junit.Test
import retrofit2.Response

class KitsuLibraryTests {
    @Test
    @Ignore("Add these later")
    fun `retrieveAnime`() {

    }

    @Test
    @Ignore("Add these later")
    fun `retrieveManga`() {

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
