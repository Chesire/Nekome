package com.chesire.nekome.kitsu.library

import com.chesire.nekome.core.Resource
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.kitsu.api.intermediaries.Links
import com.chesire.nekome.kitsu.library.dto.AddResponseDto
import com.chesire.nekome.kitsu.library.dto.DtoFactory
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response
import java.net.UnknownHostException

class KitsuLibraryTests {

    private val map = mockk<ResponseDtoMapper> {
        every { toSeriesDomain(any()) } returns createSeriesDomain()
    }
    private val factory = mockk<DtoFactory> {
        every { createAddDto(any(), any(), any(), any()) } returns ""
        every { createUpdateDto(any(), any(), any(), any()) } returns ""
    }

    @Test
    fun `retrieveAnime on success returns the retrieved models`() = runBlocking {
        val expected = createRetrieveResponseDto(SeriesType.Anime)
        val mockService = mockk<KitsuLibraryService> {
            coEvery {
                retrieveAnimeAsync(1, 0, 500)
            } coAnswers {
                mockk {
                    every { isSuccessful } returns true
                    every { body() } returns expected
                }
            }
        }
        val classUnderTest = KitsuLibrary(mockService, map, factory)
        val actual = classUnderTest.retrieveAnime(1)

        coVerify(exactly = 1) { mockService.retrieveAnimeAsync(1, 0, 500) }
        when (actual) {
            is Resource.Success -> {
                /* Pass */
            }
            is Resource.Error -> error("Test has failed")
        }
    }

    @Test
    fun `retrieveAnime more items to retrieve executes again with new offset`() = runBlocking {
        val expected = createRetrieveResponseDto(SeriesType.Anime, Links(next = "next"))
        val mockService = mockk<KitsuLibraryService> {
            coEvery {
                retrieveAnimeAsync(1, 0, 500)
            } coAnswers {
                mockk {
                    every { isSuccessful } returns true
                    every { body() } returns expected
                }
            }
            coEvery {
                retrieveAnimeAsync(1, 500, 500)
            } coAnswers {
                mockk {
                    every { isSuccessful } returns true
                    every { body() } returns createRetrieveResponseDto(SeriesType.Anime)
                }
            }
        }
        val classUnderTest = KitsuLibrary(mockService, map, factory)
        classUnderTest.retrieveAnime(1)

        coVerify(exactly = 2) { mockService.retrieveAnimeAsync(any(), any(), any()) }
    }

    @Test
    fun `retrieveAnime on error retries up to 4 times`() = runBlocking {
        val mockService = mockk<KitsuLibraryService> {
            coEvery {
                retrieveAnimeAsync(1, 0, 500)
            } coAnswers {
                mockk {
                    every { isSuccessful } returns false
                    every { code() } returns 0
                    every { body() } returns null
                    every { errorBody() } returns mockk {
                        every { string() } returns ""
                    }
                }
            }
        }
        val classUnderTest = KitsuLibrary(mockService, map, factory)
        val actual = classUnderTest.retrieveAnime(1)

        coVerify(exactly = 4) { mockService.retrieveAnimeAsync(1, 0, 500) }
        when (actual) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> assertTrue(true)
        }
    }

    @Test
    fun `retrieveAnime error is produced only if no models are found`() = runBlocking {
        val mockService = mockk<KitsuLibraryService> {
            coEvery {
                retrieveAnimeAsync(1, 0, 500)
            } coAnswers {
                mockk {
                    every { isSuccessful } returns true
                    every { body() } returns createRetrieveResponseDto(
                        SeriesType.Anime,
                        Links(next = "next")
                    )
                }
            }
            coEvery {
                retrieveAnimeAsync(1, 500, 500)
            } coAnswers {
                mockk {
                    every { isSuccessful } returns false
                    every { code() } returns 0
                    every { body() } returns createRetrieveResponseDto(SeriesType.Anime)
                    every { errorBody() } returns mockk {
                        every { string() } returns ""
                    }
                }
            }
        }
        val classUnderTest = KitsuLibrary(mockService, map, factory)
        val actual = classUnderTest.retrieveAnime(1)

        coVerify { mockService.retrieveAnimeAsync(1, 0, 500) }
        coVerify(exactly = 4) { mockService.retrieveAnimeAsync(1, 500, 500) }
        when (actual) {
            is Resource.Success -> assertTrue(true)
            is Resource.Error -> error("Test has failed")
        }
    }

    @Test
    fun `retrieveAnime on thrown exception return Resource#Error`() = runBlocking {
        val mockService = mockk<KitsuLibraryService> {
            coEvery { retrieveAnimeAsync(any(), any(), any()) } throws UnknownHostException()
        }

        val classUnderTest = KitsuLibrary(mockService, map, factory)
        val result = classUnderTest.retrieveAnime(0)

        when (result) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> assertTrue(true)
        }
    }

    @Test
    fun `retrieveManga on success returns Resource#Success`() = runBlocking {
        val expected = createRetrieveResponseDto(SeriesType.Manga)
        val mockService = mockk<KitsuLibraryService> {
            coEvery {
                retrieveMangaAsync(0, 0, 500)
            } coAnswers {
                mockk {
                    every { isSuccessful } returns true
                    every { body() } returns expected
                }
            }
        }
        val classUnderTest = KitsuLibrary(mockService, map, factory)
        val actual = classUnderTest.retrieveManga(0)

        when (actual) {
            is Resource.Success -> {
                /* Pass */
            }
            is Resource.Error -> error("Test has failed")
        }
    }

    @Test
    fun `retrieveManga more items to retrieve executes again with new offset`() = runBlocking {
        val expected = createRetrieveResponseDto(SeriesType.Manga, Links(next = "next"))
        val mockService = mockk<KitsuLibraryService> {
            coEvery {
                retrieveMangaAsync(1, 0, 500)
            } coAnswers {
                mockk {
                    every { isSuccessful } returns true
                    every { body() } returns expected
                }
            }
            coEvery {
                retrieveMangaAsync(1, 500, 500)
            } coAnswers {
                mockk {
                    every { isSuccessful } returns true
                    every { body() } returns createRetrieveResponseDto(SeriesType.Manga)
                }
            }
        }
        val classUnderTest = KitsuLibrary(mockService, map, factory)
        classUnderTest.retrieveManga(1)

        coVerify(exactly = 2) { mockService.retrieveMangaAsync(any(), any(), any()) }
    }

    @Test
    fun `retrieveManga on error retries up to 4 times`() = runBlocking {
        val mockService = mockk<KitsuLibraryService> {
            coEvery {
                retrieveMangaAsync(1, 0, 500)
            } coAnswers {
                mockk {
                    every { isSuccessful } returns false
                    every { code() } returns 0
                    every { body() } returns null
                    every { errorBody() } returns mockk {
                        every { string() } returns ""
                    }
                }
            }
        }
        val classUnderTest = KitsuLibrary(mockService, map, factory)
        val actual = classUnderTest.retrieveManga(1)

        coVerify(exactly = 4) { mockService.retrieveMangaAsync(1, 0, 500) }
        when (actual) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> assertTrue(true)
        }
    }

    @Test
    fun `retrieveManga error is produced only if no models are found`() = runBlocking {
        val mockService = mockk<KitsuLibraryService> {
            coEvery {
                retrieveMangaAsync(1, 0, 500)
            } coAnswers {
                mockk {
                    every { isSuccessful } returns true
                    every { body() } returns createRetrieveResponseDto(
                        SeriesType.Manga,
                        Links(next = "next")
                    )
                }
            }
            coEvery {
                retrieveMangaAsync(1, 500, 500)
            } coAnswers {
                mockk {
                    every { isSuccessful } returns false
                    every { code() } returns 0
                    every { body() } returns createRetrieveResponseDto(SeriesType.Manga)
                    every { errorBody() } returns mockk {
                        every { string() } returns ""
                    }
                }
            }
        }
        val classUnderTest = KitsuLibrary(mockService, map, factory)
        val actual = classUnderTest.retrieveManga(1)

        coVerify { mockService.retrieveMangaAsync(1, 0, 500) }
        coVerify(exactly = 4) { mockService.retrieveMangaAsync(1, 500, 500) }
        when (actual) {
            is Resource.Success -> assertTrue(true)
            is Resource.Error -> error("Test has failed")
        }
    }

    @Test
    fun `retrieveManga on thrown exception return Resource#Error`() = runBlocking {
        val mockService = mockk<KitsuLibraryService> {
            coEvery {
                retrieveMangaAsync(any(), any(), any())
            } throws UnknownHostException()
        }

        val classUnderTest = KitsuLibrary(mockService, map, factory)
        val result = classUnderTest.retrieveManga(0)

        when (result) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> assertTrue(true)
        }
    }

    @Test
    fun `addAnime success return Resource#Error`() = runBlocking {
        val mockModel = createAddResponseDto(SeriesType.Anime)
        val mockResponse = mockk<Response<AddResponseDto>> {
            every { isSuccessful } returns true
            every { body() } returns mockModel
        }
        val mockService = mockk<KitsuLibraryService> {
            coEvery {
                addAnimeAsync(any())
            } coAnswers {
                mockResponse
            }
        }

        val classUnderTest = KitsuLibrary(mockService, map, factory)
        val actual = classUnderTest.addAnime(1, 10, UserSeriesStatus.Planned)

        when (actual) {
            is Resource.Success -> {
                /* Pass */
            }
            is Resource.Error -> error("Test has failed")
        }
    }

    @Test
    fun `addAnime on thrown exception return Resource#Error`() = runBlocking {
        val mockService = mockk<KitsuLibraryService> {
            coEvery { addAnimeAsync(any()) } throws UnknownHostException()
        }

        val classUnderTest = KitsuLibrary(mockService, map, factory)
        val result = classUnderTest.addAnime(0, 0, UserSeriesStatus.Dropped)

        when (result) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> assertTrue(true)
        }
    }

    @Test
    fun `addManga on success return Resource#Error`() = runBlocking {
        val mockModel = createAddResponseDto(SeriesType.Manga)
        val mockResponse = mockk<Response<AddResponseDto>> {
            every { isSuccessful } returns true
            every { body() } returns mockModel
        }
        val mockService = mockk<KitsuLibraryService> {
            coEvery {
                addMangaAsync(any())
            } coAnswers {
                mockResponse
            }
        }

        val classUnderTest = KitsuLibrary(mockService, map, factory)
        val actual = classUnderTest.addManga(1, 10, UserSeriesStatus.Planned)

        when (actual) {
            is Resource.Success -> {
                /* Pass */
            }
            is Resource.Error -> error("Test has failed")
        }
    }

    @Test
    fun `addManga on thrown exception return Resource#Error`() = runBlocking {
        val mockService = mockk<KitsuLibraryService> {
            coEvery { addMangaAsync(any()) } throws UnknownHostException()
        }

        val classUnderTest = KitsuLibrary(mockService, map, factory)
        val result = classUnderTest.addManga(0, 0, UserSeriesStatus.Dropped)

        when (result) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> assertTrue(true)
        }
    }

    @Test
    fun `update success returns Resource#Success`() = runBlocking {
        val mockModel = createAddResponseDto(SeriesType.Manga)
        val mockResponse = mockk<Response<AddResponseDto>> {
            every { isSuccessful } returns true
            every { body() } returns mockModel
        }
        val mockService = mockk<KitsuLibraryService> {
            coEvery {
                updateItemAsync(10, any())
            } coAnswers {
                mockResponse
            }
        }

        val classUnderTest = KitsuLibrary(mockService, map, factory)
        val actual = classUnderTest.update(10, 0, UserSeriesStatus.OnHold, 0)

        when (actual) {
            is Resource.Success -> {
                /* Pass */
            }
            is Resource.Error -> error("Test has failed")
        }
    }

    @Test
    fun `update success returns Resource#Error if no body`() = runBlocking {
        val expected = "Response body is null"
        val mockResponse = mockk<Response<AddResponseDto>> {
            every { isSuccessful } returns true
            every { body() } returns null
        }
        val mockService = mockk<KitsuLibraryService> {
            coEvery {
                updateItemAsync(10, any())
            } coAnswers {
                mockResponse
            }
        }

        val classUnderTest = KitsuLibrary(mockService, map, factory)
        val actual = classUnderTest.update(10, 0, UserSeriesStatus.OnHold, 0)

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
        val mockResponse = mockk<Response<AddResponseDto>> {
            every { isSuccessful } returns false
            every { errorBody() } returns mockResponseBody
            every { code() } returns 0
        }
        val mockService = mockk<KitsuLibraryService> {
            coEvery {
                updateItemAsync(10, any())
            } coAnswers {
                mockResponse
            }
        }

        val classUnderTest = KitsuLibrary(mockService, map, factory)
        val actual = classUnderTest.update(10, 0, UserSeriesStatus.OnHold, 0)

        when (actual) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> assertSame(expected, actual.msg)
        }
    }

    @Test
    fun `update failure returns Resource#Error with message if null errorBody`() = runBlocking {
        val expected = "errorBodyString"

        val mockResponse = mockk<Response<AddResponseDto>> {
            every { isSuccessful } returns false
            every { errorBody() } returns null
            every { message() } returns expected
            every { code() } returns 0
        }
        val mockService = mockk<KitsuLibraryService> {
            coEvery {
                updateItemAsync(10, any())
            } coAnswers {
                mockResponse
            }
        }

        val classUnderTest = KitsuLibrary(mockService, map, factory)
        val actual = classUnderTest.update(10, 0, UserSeriesStatus.OnHold, 0)

        when (actual) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> assertSame(expected, actual.msg)
        }
    }

    @Test
    fun `update on thrown exception return Resource#Error`() = runBlocking {
        val mockService = mockk<KitsuLibraryService> {
            coEvery { updateItemAsync(any(), any()) } throws UnknownHostException()
        }

        val classUnderTest = KitsuLibrary(mockService, map, factory)
        val result = classUnderTest.update(0, 0, UserSeriesStatus.Current, 0)

        when (result) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> assertTrue(true)
        }
    }

    @Test
    fun `delete success returns Resource#Success`() = runBlocking {
        val mockResponse = mockk<Response<Any>> {
            every { isSuccessful } returns true
        }
        val mockService = mockk<KitsuLibraryService> {
            coEvery {
                deleteItemAsync(10)
            } coAnswers {
                mockResponse
            }
        }

        val classUnderTest = KitsuLibrary(mockService, map, factory)
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
            coEvery {
                deleteItemAsync(10)
            } coAnswers {
                mockResponse
            }
        }

        val classUnderTest = KitsuLibrary(mockService, map, factory)
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
            coEvery {
                deleteItemAsync(10)
            } coAnswers {
                mockResponse
            }
        }

        val classUnderTest = KitsuLibrary(mockService, map, factory)
        val actual = classUnderTest.delete(10)

        when (actual) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> assertEquals(expected, actual.msg)
        }
    }

    @Test
    fun `delete on thrown exception return Resource#Error`() = runBlocking {
        val mockService = mockk<KitsuLibraryService> {
            coEvery { deleteItemAsync(any()) } throws UnknownHostException()
        }

        val classUnderTest = KitsuLibrary(mockService, map, factory)
        val result = classUnderTest.delete(0)

        when (result) {
            is Resource.Success -> error("Test has failed")
            is Resource.Error -> assertTrue(true)
        }
    }
}
