package com.chesire.nekome.kitsu.library

import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.kitsu.api.intermediaries.Links
import com.chesire.nekome.kitsu.library.dto.AddResponseDto
import com.chesire.nekome.kitsu.library.dto.DtoFactory
import com.github.michaelbull.result.get
import com.github.michaelbull.result.getError
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import java.net.UnknownHostException
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertSame
import org.junit.Test
import retrofit2.Response

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
        val actual = classUnderTest.retrieveAnime(1).get()

        coVerify(exactly = 1) { mockService.retrieveAnimeAsync(1, 0, 500) }
        assertNotNull(actual)
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
        val result = classUnderTest.retrieveAnime(1).getError()

        coVerify(exactly = 4) { mockService.retrieveAnimeAsync(1, 0, 500) }
        assertNotNull(result)
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
        val result = classUnderTest.retrieveAnime(1).get()

        coVerify { mockService.retrieveAnimeAsync(1, 0, 500) }
        coVerify(exactly = 4) { mockService.retrieveAnimeAsync(1, 500, 500) }
        assertNotNull(result)
    }

    @Test
    fun `retrieveAnime on thrown exception return Err`() = runBlocking {
        val mockService = mockk<KitsuLibraryService> {
            coEvery { retrieveAnimeAsync(any(), any(), any()) } throws UnknownHostException()
        }

        val classUnderTest = KitsuLibrary(mockService, map, factory)
        val result = classUnderTest.retrieveAnime(0).getError()

        assertNotNull(result)
    }

    @Test
    fun `retrieveManga on success returns Ok`() = runBlocking {
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
        val result = classUnderTest.retrieveManga(0).get()

        assertNotNull(result)
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
        val result = classUnderTest.retrieveManga(1).getError()

        coVerify(exactly = 4) { mockService.retrieveMangaAsync(1, 0, 500) }
        assertNotNull(result)
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
        val result = classUnderTest.retrieveManga(1).get()

        coVerify { mockService.retrieveMangaAsync(1, 0, 500) }
        coVerify(exactly = 4) { mockService.retrieveMangaAsync(1, 500, 500) }
        assertNotNull(result)
    }

    @Test
    fun `retrieveManga on thrown exception return Err`() = runBlocking {
        val mockService = mockk<KitsuLibraryService> {
            coEvery {
                retrieveMangaAsync(any(), any(), any())
            } throws UnknownHostException()
        }

        val classUnderTest = KitsuLibrary(mockService, map, factory)
        val result = classUnderTest.retrieveManga(0).getError()

        assertNotNull(result)
    }

    @Test
    fun `addAnime success return Ok`() = runBlocking {
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
        val result = classUnderTest.addAnime(1, 10, UserSeriesStatus.Planned).get()

        assertNotNull(result)
    }

    @Test
    fun `addAnime on thrown exception return Err`() = runBlocking {
        val mockService = mockk<KitsuLibraryService> {
            coEvery { addAnimeAsync(any()) } throws UnknownHostException()
        }

        val classUnderTest = KitsuLibrary(mockService, map, factory)
        val result = classUnderTest.addAnime(0, 0, UserSeriesStatus.Dropped).getError()

        assertNotNull(result)
    }

    @Test
    fun `addManga on success return Err`() = runBlocking {
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
        val result = classUnderTest.addManga(1, 10, UserSeriesStatus.Planned).get()

        assertNotNull(result)
    }

    @Test
    fun `addManga on thrown exception return Err`() = runBlocking {
        val mockService = mockk<KitsuLibraryService> {
            coEvery { addMangaAsync(any()) } throws UnknownHostException()
        }

        val classUnderTest = KitsuLibrary(mockService, map, factory)
        val result = classUnderTest.addManga(0, 0, UserSeriesStatus.Dropped).getError()

        assertNotNull(result)
    }

    @Test
    fun `update success returns Ok`() = runBlocking {
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
        val result = classUnderTest.update(10, 0, UserSeriesStatus.OnHold, 0).get()

        assertNotNull(result)
    }

    @Test
    fun `update success returns Err if no body`() = runBlocking {
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
        val actual = classUnderTest.update(10, 0, UserSeriesStatus.OnHold, 0).getError()

        assertSame(expected, actual?.message)
    }

    @Test
    fun `update failure returns Err with errorBody`() = runBlocking {
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
        val actual = classUnderTest.update(10, 0, UserSeriesStatus.OnHold, 0).getError()

        assertSame(expected, actual?.message)
    }

    @Test
    fun `update failure returns Err with message if null errorBody`() = runBlocking {
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
        val actual = classUnderTest.update(10, 0, UserSeriesStatus.OnHold, 0).getError()

        assertSame(expected, actual?.message)
    }

    @Test
    fun `update on thrown exception return Err`() = runBlocking {
        val mockService = mockk<KitsuLibraryService> {
            coEvery { updateItemAsync(any(), any()) } throws UnknownHostException()
        }

        val classUnderTest = KitsuLibrary(mockService, map, factory)
        val result = classUnderTest.update(0, 0, UserSeriesStatus.Current, 0).getError()

        assertNotNull(result)
    }

    @Test
    fun `delete success returns Ok`() = runBlocking {
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
        val result = classUnderTest.delete(10).get()

        assertNotNull(result)
    }

    @Test
    fun `delete failure returns Err with errorBody`() = runBlocking {
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
        val actual = classUnderTest.delete(10).getError()

        assertEquals(expected, actual?.message)
    }

    @Test
    fun `delete failure returns Err with message if null errorBody`() = runBlocking {
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
        val actual = classUnderTest.delete(10).getError()

        assertEquals(expected, actual?.message)
    }

    @Test
    fun `delete on thrown exception return Err`() = runBlocking {
        val mockService = mockk<KitsuLibraryService> {
            coEvery { deleteItemAsync(any()) } throws UnknownHostException()
        }

        val classUnderTest = KitsuLibrary(mockService, map, factory)
        val result = classUnderTest.delete(0).getError()

        assertNotNull(result)
    }
}
