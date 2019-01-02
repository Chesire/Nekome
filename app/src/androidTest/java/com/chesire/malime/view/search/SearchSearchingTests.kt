package com.chesire.malime.view.search

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.chesire.malime.INVALID_SEARCH
import com.chesire.malime.R
import com.chesire.malime.VALID_SEARCH_MULTIPLE_ITEMS
import com.chesire.malime.VALID_SEARCH_NO_ITEMS
import com.chesire.malime.VALID_SEARCH_SINGLE_ITEM
import com.chesire.malime.core.api.SearchApi
import com.chesire.malime.core.flags.ItemType
import com.chesire.malime.getMalimeModel
import com.chesire.malime.injection.espressoDaggerMockRule
import com.chesire.malime.tools.ToastMatcher.Companion.onToast
import com.chesire.malime.view.MainActivity
import com.schibsted.spain.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaRadioButtonInteractions.clickRadioButtonItem
import com.schibsted.spain.barista.interaction.BaristaSleepInteractions.sleep
import io.reactivex.Single
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
class SearchSearchingTests {
    @get:Rule
    var daggerRule = espressoDaggerMockRule()

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java, false, false)

    @Mock
    private lateinit var searchApi: SearchApi

    @Test
    fun noAnimeSearchIsPerformedWithEmptySearchText() {
        activityRule.launchActivity(null)
        clickOn(R.id.menuMainNavigationSearch)
        clickRadioButtonItem(
            R.id.fragmentSearchOptionChoices,
            R.id.fragmentSearchOptionAnimeChoice
        )
        onView(withId(R.id.fragmentSearchSearchEditText)).perform(
            clearText(),
            pressImeActionButton()
        )

        onToast(R.string.search_failed_no_items).check(doesNotExist())
        onToast(R.string.search_failed_general_error)
            .check(doesNotExist())
    }

    @Test
    fun noMangaSearchIsPerformedWithEmptySearchText() {
        activityRule.launchActivity(null)
        clickOn(R.id.menuMainNavigationSearch)
        clickRadioButtonItem(
            R.id.fragmentSearchOptionChoices,
            R.id.fragmentSearchOptionMangaChoice
        )
        onView(withId(R.id.fragmentSearchSearchEditText)).perform(
            clearText(),
            pressImeActionButton()
        )

        onToast(R.string.search_failed_no_items).check(doesNotExist())
        onToast(R.string.search_failed_general_error)
            .check(doesNotExist())
    }

    @Test
    fun failedAnimeSearchProducesError() {
        Mockito.`when`(
            searchApi.searchForSeriesWith(
                INVALID_SEARCH,
                ItemType.Anime
            )
        ).thenReturn(Single.error { Throwable("failedAnimeSearchProducesError") })

        activityRule.launchActivity(null)
        clickOn(R.id.menuMainNavigationSearch)
        clickRadioButtonItem(
            R.id.fragmentSearchOptionChoices,
            R.id.fragmentSearchOptionAnimeChoice
        )
        onView(withId(R.id.fragmentSearchSearchEditText)).perform(
            typeText(INVALID_SEARCH),
            pressImeActionButton()
        )

        onToast(R.string.search_failed_general_error)
            .check(matches(isDisplayed()))
    }

    @Test
    fun failedMangaSearchProducesError() {
        Mockito.`when`(
            searchApi.searchForSeriesWith(
                INVALID_SEARCH,
                ItemType.Manga
            )
        ).thenReturn(Single.error { Throwable("failedAnimeSearchProducesError") })

        activityRule.launchActivity(null)
        clickOn(R.id.menuMainNavigationSearch)
        clickRadioButtonItem(
            R.id.fragmentSearchOptionChoices,
            R.id.fragmentSearchOptionMangaChoice
        )
        onView(withId(R.id.fragmentSearchSearchEditText)).perform(
            typeText(INVALID_SEARCH),
            pressImeActionButton()
        )

        onToast(R.string.search_failed_general_error)
            .check(matches(isDisplayed()))
    }

    @Test
    fun animeSearchWithNoItemsProducesError() {
        Mockito.`when`(
            searchApi.searchForSeriesWith(
                VALID_SEARCH_NO_ITEMS,
                ItemType.Anime
            )
        ).thenReturn(Single.just(listOf()))

        activityRule.launchActivity(null)
        clickOn(R.id.menuMainNavigationSearch)
        clickRadioButtonItem(
            R.id.fragmentSearchOptionChoices,
            R.id.fragmentSearchOptionAnimeChoice
        )
        onView(withId(R.id.fragmentSearchSearchEditText)).perform(
            typeText(VALID_SEARCH_NO_ITEMS),
            pressImeActionButton()
        )

        onToast(R.string.search_failed_no_items)
            .check(matches(isDisplayed()))
    }

    @Test
    fun mangaSearchWithNoItemsProducesError() {
        Mockito.`when`(
            searchApi.searchForSeriesWith(
                VALID_SEARCH_NO_ITEMS,
                ItemType.Manga
            )
        ).thenReturn(Single.just(listOf()))

        activityRule.launchActivity(null)
        clickOn(R.id.menuMainNavigationSearch)
        clickRadioButtonItem(
            R.id.fragmentSearchOptionChoices,
            R.id.fragmentSearchOptionMangaChoice
        )
        onView(withId(R.id.fragmentSearchSearchEditText)).perform(
            typeText(VALID_SEARCH_NO_ITEMS),
            pressImeActionButton()
        )

        onToast(R.string.search_failed_no_items)
            .check(matches(isDisplayed()))
    }

    @Test
    fun successfulAnimeSearchHasListOfSingleSearchItem() {
        Mockito.`when`(
            searchApi.searchForSeriesWith(
                VALID_SEARCH_SINGLE_ITEM,
                ItemType.Anime
            )
        ).thenReturn(Single.just(listOf(getMalimeModel(0))))

        activityRule.launchActivity(null)
        clickOn(R.id.menuMainNavigationSearch)
        clickRadioButtonItem(
            R.id.fragmentSearchOptionChoices,
            R.id.fragmentSearchOptionAnimeChoice
        )
        onView(withId(R.id.fragmentSearchSearchEditText)).perform(
            typeText(VALID_SEARCH_SINGLE_ITEM),
            pressImeActionButton()
        )

        sleep(100)
        assertRecyclerViewItemCount(R.id.fragmentSearchRecyclerView, 1)
    }

    @Test
    fun successfulMangaSearchHasListOfSingleSearchItem() {
        Mockito.`when`(
            searchApi.searchForSeriesWith(
                VALID_SEARCH_SINGLE_ITEM,
                ItemType.Manga
            )
        ).thenReturn(Single.just(listOf(getMalimeModel(0))))

        activityRule.launchActivity(null)
        clickOn(R.id.menuMainNavigationSearch)
        clickRadioButtonItem(
            R.id.fragmentSearchOptionChoices,
            R.id.fragmentSearchOptionMangaChoice
        )
        onView(withId(R.id.fragmentSearchSearchEditText)).perform(
            typeText(VALID_SEARCH_SINGLE_ITEM),
            pressImeActionButton()
        )

        sleep(100)
        assertRecyclerViewItemCount(R.id.fragmentSearchRecyclerView, 1)
    }

    @Test
    fun successfulAnimeSearchHasListOfSearchItems() {
        Mockito.`when`(
            searchApi.searchForSeriesWith(
                VALID_SEARCH_MULTIPLE_ITEMS,
                ItemType.Anime
            )
        ).thenReturn(
            Single.just(
                listOf(
                    getMalimeModel(seriesId = 0),
                    getMalimeModel(seriesId = 1),
                    getMalimeModel(seriesId = 2),
                    getMalimeModel(seriesId = 3),
                    getMalimeModel(seriesId = 4),
                    getMalimeModel(seriesId = 5),
                    getMalimeModel(seriesId = 6),
                    getMalimeModel(seriesId = 7),
                    getMalimeModel(seriesId = 8),
                    getMalimeModel(seriesId = 9),
                    getMalimeModel(seriesId = 10)
                )
            )
        )

        activityRule.launchActivity(null)
        clickOn(R.id.menuMainNavigationSearch)
        clickRadioButtonItem(
            R.id.fragmentSearchOptionChoices,
            R.id.fragmentSearchOptionAnimeChoice
        )
        onView(withId(R.id.fragmentSearchSearchEditText)).perform(
            typeText(VALID_SEARCH_MULTIPLE_ITEMS),
            pressImeActionButton()
        )

        sleep(100)
        assertRecyclerViewItemCount(R.id.fragmentSearchRecyclerView, 11)
    }

    @Test
    fun successfulMangaSearchHasListOfSearchItems() {
        Mockito.`when`(
            searchApi.searchForSeriesWith(
                VALID_SEARCH_MULTIPLE_ITEMS,
                ItemType.Manga
            )
        ).thenReturn(
            Single.just(
                listOf(
                    getMalimeModel(seriesId = 0),
                    getMalimeModel(seriesId = 1),
                    getMalimeModel(seriesId = 2),
                    getMalimeModel(seriesId = 3),
                    getMalimeModel(seriesId = 4),
                    getMalimeModel(seriesId = 5),
                    getMalimeModel(seriesId = 6),
                    getMalimeModel(seriesId = 7),
                    getMalimeModel(seriesId = 8),
                    getMalimeModel(seriesId = 9),
                    getMalimeModel(seriesId = 10)
                )
            )
        )

        activityRule.launchActivity(null)
        clickOn(R.id.menuMainNavigationSearch)
        clickRadioButtonItem(
            R.id.fragmentSearchOptionChoices,
            R.id.fragmentSearchOptionMangaChoice
        )
        onView(withId(R.id.fragmentSearchSearchEditText)).perform(
            typeText(VALID_SEARCH_MULTIPLE_ITEMS),
            pressImeActionButton()
        )

        sleep(100)
        assertRecyclerViewItemCount(R.id.fragmentSearchRecyclerView, 11)
    }

    // Need to add test for if item is already saved, that UI is different on search
}
