package com.chesire.nekome.features.series

import androidx.compose.ui.test.junit4.createComposeRule
import com.chesire.nekome.UITest
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.models.ErrorDomain
import com.chesire.nekome.datasource.series.remote.SeriesApi
import com.chesire.nekome.helpers.creation.createSeriesDomain
import com.chesire.nekome.injection.LibraryModule
import com.chesire.nekome.robots.series.seriesList
import com.chesire.nekome.testing.createSeriesEntity
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(LibraryModule::class)
class SeriesListTests : UITest() {

    @BindValue
    val seriesApi = mockk<SeriesApi>()

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun canReachSeriesList() {
        launchActivity()

        seriesList(composeTestRule) {
            validate { isVisible() }
        }
    }

    @Test
    fun emptyListDisplaysEmptyView() {
        launchActivity()

        seriesList(composeTestRule) {
            validate { isEmptyDisplayed() }
        }
    }

    @Test
    fun populatedListDisplaysListView() {
        runBlocking {
            series.insert(createSeriesEntity())
        }
        launchActivity()

        seriesList(composeTestRule) {
            validate {
                isListDisplayed()
                listCount(1)
            }
        }
    }

    @Test
    fun refreshingLayoutAddsNewItems() {
        seriesApi.apply {
            coEvery {
                retrieveAnime(any())
            } coAnswers {
                Ok(
                    listOf(
                        createSeriesDomain(
                            id = 0,
                            userId = 0,
                            title = "Series 0",
                            seriesType = SeriesType.Anime,
                            userSeriesStatus = UserSeriesStatus.Current
                        ),
                        createSeriesDomain(
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
                Ok(listOf())
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

        seriesList(composeTestRule) {
            refreshList()
        } validate {
            listCount(2)
        }
    }

    @Test
    fun failureToRefreshSeriesShowsError() {
        seriesApi.apply {
            coEvery {
                retrieveAnime(any())
            } coAnswers {
                Err(ErrorDomain.couldNotReach)
            }
            coEvery {
                retrieveManga(any())
            } coAnswers {
                Err(ErrorDomain.couldNotReach)
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

        seriesList(composeTestRule) {
            refreshList()
        } validate {
            isRefreshSeriesError()
        }
    }

    @Test
    fun canIncrementSeriesProgress() {
        coEvery {
            seriesApi.update(0, 1, UserSeriesStatus.Current, 0)
        } coAnswers {
            Ok(
                createSeriesDomain(
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

        seriesList(composeTestRule) {
            incrementSeriesByOne(0)
        } validate {
            seriesProgress(0, 1, 24)
        }
    }

    @Test
    fun completingSeriesWithoutRateOnCompletionSettingShowsNoDialog() {
        coEvery {
            seriesApi.update(0, 24, UserSeriesStatus.Current, 0)
        } coAnswers {
            Ok(
                createSeriesDomain(
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
            seriesPreferences.updateRateSeriesOnCompletion(false)
        }
        launchActivity()

        seriesList(composeTestRule) {
            incrementSeriesByOne(0)
        } validate {
            isRatingDialogNotDisplayed()
        }
    }

    @Test
    fun completingSeriesWithRateOnCompletionSettingShowsRateDialog() {
        coEvery {
            seriesApi.update(0, 24, UserSeriesStatus.Current, 0)
        } coAnswers {
            Ok(
                createSeriesDomain(
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
            seriesPreferences.updateRateSeriesOnCompletion(true)
        }
        launchActivity()

        seriesList(composeTestRule) {
            incrementSeriesByOne(0)
        } validate {
            isRatingDialogDisplayed()
        }
    }
}
