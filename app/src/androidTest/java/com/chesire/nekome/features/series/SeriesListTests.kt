package com.chesire.nekome.features.series

import com.chesire.nekome.UITest
import com.chesire.nekome.app.series.SeriesPreferences
import com.chesire.nekome.core.Resource
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.helpers.creation.createLibraryDomain
import com.chesire.nekome.injection.LibraryModule
import com.chesire.nekome.library.api.LibraryApi
import com.chesire.nekome.robots.series.seriesList
import com.chesire.nekome.testing.createSeriesEntity
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(LibraryModule::class)
class SeriesListTests : UITest() {

    @BindValue
    val libraryApi = mockk<LibraryApi>()

    @Inject
    lateinit var seriesPreferences: SeriesPreferences

    @Test
    fun canReachSeriesList() {
        launchActivity()

        seriesList {
            validate { isVisible() }
        }
    }

    @Test
    fun emptyListDisplaysEmptyView() {
        launchActivity()

        seriesList {
            validate { isEmptyDisplayed() }
        }
    }

    @Test
    fun populatedListDisplaysListView() {
        runBlocking {
            series.insert(createSeriesEntity())
        }
        launchActivity()

        seriesList {
            validate {
                isListDisplayed()
                listCount(1)
            }
        }
    }

    @Test
    fun refreshingLayoutAddsNewItems() {
        libraryApi.apply {
            coEvery {
                retrieveAnime(any())
            } coAnswers {
                Resource.Success(
                    listOf(
                        createLibraryDomain(
                            id = 0,
                            userId = 0,
                            title = "Series 0",
                            seriesType = SeriesType.Anime,
                            userSeriesStatus = UserSeriesStatus.Current
                        ),
                        createLibraryDomain(
                            id = 1,
                            userId = 1,
                            title = "Series 1",
                            seriesType = SeriesType.Anime,
                            userSeriesStatus = UserSeriesStatus.Current
                        )
                    )
                )
            }
            coEvery {
                retrieveManga(any())
            } coAnswers {
                Resource.Success(listOf())
            }
        }
        runBlocking {
            series.insert(
                createSeriesEntity(
                    id = 0,
                    userId = 0,
                    title = "Series 0",
                    seriesType = SeriesType.Anime,
                    userSeriesStatus = UserSeriesStatus.Current
                )
            )
        }
        launchActivity()

        seriesList {
            refreshList()
        } validate {
            listCount(2)
        }
    }

    @Test
    fun failureToRefreshSeriesShowsError() {
        libraryApi.apply {
            coEvery {
                retrieveAnime(any())
            } coAnswers {
                Resource.Error.couldNotReach()
            }
            coEvery {
                retrieveManga(any())
            } coAnswers {
                Resource.Error.couldNotReach()
            }
        }
        runBlocking {
            series.insert(
                createSeriesEntity(
                    id = 0,
                    userId = 0,
                    title = "Series 0",
                    seriesType = SeriesType.Anime,
                    userSeriesStatus = UserSeriesStatus.Current
                )
            )
        }
        launchActivity()

        seriesList {
            refreshList()
        } validate {
            isRefreshSeriesError()
        }
    }

    @Test
    fun canIncrementSeriesProgress() {
        coEvery {
            libraryApi.update(0, 1, UserSeriesStatus.Current, 0)
        } coAnswers {
            Resource.Success(
                createLibraryDomain(
                    id = 0,
                    userId = 0,
                    title = "Series 0",
                    seriesType = SeriesType.Anime,
                    userSeriesStatus = UserSeriesStatus.Current,
                    progress = 1,
                    totalLength = 24
                )
            )
        }
        runBlocking {
            series.insert(
                createSeriesEntity(
                    id = 0,
                    userId = 0,
                    title = "Series 0",
                    seriesType = SeriesType.Anime,
                    userSeriesStatus = UserSeriesStatus.Current,
                    progress = 0,
                    totalLength = 24
                )
            )
        }
        launchActivity()

        seriesList {
            incrementSeriesByOne(0)
        } validate {
            seriesProgress(0, 1, 24)
        }
    }

    @Test
    fun completingSeriesWithoutRateOnCompletionSettingShowsNoDialog() {
        coEvery {
            libraryApi.update(0, 24, UserSeriesStatus.Current, 0)
        } coAnswers {
            Resource.Success(
                createLibraryDomain(
                    id = 0,
                    userId = 0,
                    title = "Series 0",
                    seriesType = SeriesType.Anime,
                    userSeriesStatus = UserSeriesStatus.Completed,
                    progress = 24,
                    totalLength = 24
                )
            )
        }
        runBlocking {
            series.insert(
                createSeriesEntity(
                    id = 0,
                    userId = 0,
                    title = "Series 0",
                    seriesType = SeriesType.Anime,
                    userSeriesStatus = UserSeriesStatus.Current,
                    progress = 23,
                    totalLength = 24
                )
            )
        }
        seriesPreferences.rateSeriesOnCompletion = false
        launchActivity()

        seriesList {
            incrementSeriesByOne(0)
        } validate {
            isRatingDialogNotDisplayed()
        }
    }

    @Test
    fun completingSeriesWithRateOnCompletionSettingShowsRateDialog() {
        coEvery {
            libraryApi.update(0, 24, UserSeriesStatus.Current, 0)
        } coAnswers {
            Resource.Success(
                createLibraryDomain(
                    id = 0,
                    userId = 0,
                    title = "Series 0",
                    seriesType = SeriesType.Anime,
                    userSeriesStatus = UserSeriesStatus.Completed,
                    progress = 24,
                    totalLength = 24
                )
            )
        }
        runBlocking {
            series.insert(
                createSeriesEntity(
                    id = 0,
                    userId = 0,
                    title = "Series 0",
                    seriesType = SeriesType.Anime,
                    userSeriesStatus = UserSeriesStatus.Current,
                    progress = 23,
                    totalLength = 24
                )
            )
        }
        seriesPreferences.rateSeriesOnCompletion = true
        launchActivity()

        seriesList {
            incrementSeriesByOne(0)
        } validate {
            isRatingDialogDisplayed()
        }
    }
}
