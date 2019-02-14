package com.chesire.malime.view

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.chesire.malime.R
import com.chesire.malime.core.repositories.Authorization
import com.chesire.malime.injection.espressoDaggerMockRule
import com.chesire.malime.util.SharedPref
import com.chesire.malime.view.login.LoginActivity
import com.chesire.malime.view.preferences.PrefActivity
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`

@RunWith(AndroidJUnit4::class)
class MainActivityTests {
    @get:Rule
    var daggerRule = espressoDaggerMockRule()

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java, false, false)

    @Mock
    lateinit var authorization: Authorization

    @Mock
    lateinit var sharedPref: SharedPref

    @Before
    fun setUp() {
        Intents.init()
        `when`(sharedPref.forceBlockServices).thenReturn(true)
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun canNavigateToAnimeView() {
        `when`(sharedPref.appStartingScreen).thenReturn(NavigationScreen.Anime)
        activityRule.launchActivity(null)
        clickOn(R.id.menuMainNavigationAnime)

        onView(withId(R.id.menuMainNavigationAnime)).check(
            matches(withBottomNavItemCheckedStatus(true))
        )
        onView(withId(R.id.menuMainNavigationManga)).check(
            matches(withBottomNavItemCheckedStatus(false))
        )
        onView(withId(R.id.menuMainNavigationSearch)).check(
            matches(withBottomNavItemCheckedStatus(false))
        )
        assertDisplayed(R.id.fragmentMaldisplayLayout)
    }

    @Test
    fun canNavigateToMangaView() {
        `when`(sharedPref.appStartingScreen).thenReturn(NavigationScreen.Anime)
        activityRule.launchActivity(null)
        clickOn(R.id.menuMainNavigationManga)

        onView(withId(R.id.menuMainNavigationAnime)).check(
            matches(withBottomNavItemCheckedStatus(false))
        )
        onView(withId(R.id.menuMainNavigationManga)).check(
            matches(withBottomNavItemCheckedStatus(true))
        )
        onView(withId(R.id.menuMainNavigationSearch)).check(
            matches(withBottomNavItemCheckedStatus(false))
        )
        assertDisplayed(R.id.fragmentMaldisplayLayout)
    }

    @Test
    fun canNavigateToSearchView() {
        `when`(sharedPref.appStartingScreen).thenReturn(NavigationScreen.Anime)
        activityRule.launchActivity(null)
        clickOn(R.id.menuMainNavigationSearch)

        onView(withId(R.id.menuMainNavigationAnime)).check(
            matches(withBottomNavItemCheckedStatus(false))
        )
        onView(withId(R.id.menuMainNavigationManga)).check(
            matches(withBottomNavItemCheckedStatus(false))
        )
        onView(withId(R.id.menuMainNavigationSearch)).check(
            matches(withBottomNavItemCheckedStatus(true))
        )
        assertDisplayed(R.id.fragmentSearchLayout)
    }

    @Test
    fun activityCanStartInAnimeView() {
        `when`(sharedPref.appStartingScreen).thenReturn(NavigationScreen.Anime)
        activityRule.launchActivity(null)

        onView(withId(R.id.menuMainNavigationAnime)).check(
            matches(withBottomNavItemCheckedStatus(true))
        )
        onView(withId(R.id.menuMainNavigationManga)).check(
            matches(withBottomNavItemCheckedStatus(false))
        )
        onView(withId(R.id.menuMainNavigationSearch)).check(
            matches(withBottomNavItemCheckedStatus(false))
        )
        assertDisplayed(R.id.fragmentMaldisplayLayout)
    }

    @Test
    fun activityCanStartInMangaView() {
        `when`(sharedPref.appStartingScreen).thenReturn(NavigationScreen.Manga)
        activityRule.launchActivity(null)

        onView(withId(R.id.menuMainNavigationAnime)).check(
            matches(withBottomNavItemCheckedStatus(false))
        )
        onView(withId(R.id.menuMainNavigationManga)).check(
            matches(withBottomNavItemCheckedStatus(true))
        )
        onView(withId(R.id.menuMainNavigationSearch)).check(
            matches(withBottomNavItemCheckedStatus(false))
        )
        assertDisplayed(R.id.fragmentMaldisplayLayout)
    }

    @Test
    fun activityCanStartInSearchView() {
        `when`(sharedPref.appStartingScreen).thenReturn(NavigationScreen.Search)
        activityRule.launchActivity(null)

        onView(withId(R.id.menuMainNavigationAnime)).check(
            matches(withBottomNavItemCheckedStatus(false))
        )
        onView(withId(R.id.menuMainNavigationManga)).check(
            matches(withBottomNavItemCheckedStatus(false))
        )
        onView(withId(R.id.menuMainNavigationSearch)).check(
            matches(withBottomNavItemCheckedStatus(true))
        )
        assertDisplayed(R.id.fragmentSearchLayout)
    }

    @Test
    @Ignore("Couldn't find anything that says how this could be tested, will need to at some point")
    fun canLaunchProfile() {
    }

    @Test
    fun canLaunchPreferencesFromMalDisplay() {
        `when`(sharedPref.appStartingScreen).thenReturn(NavigationScreen.Anime)
        activityRule.launchActivity(null)
        clickOn(R.id.menuMainNavigationAnime)
        openActionBarOverflowOrOptionsMenu(activityRule.activity)
        clickOn(R.string.options_settings)

        Intents.intended(hasComponent(PrefActivity::class.java.name))
    }

    @Test
    fun canLaunchPreferencesFromSearch() {
        `when`(sharedPref.appStartingScreen).thenReturn(NavigationScreen.Anime)
        activityRule.launchActivity(null)
        clickOn(R.id.menuMainNavigationSearch)
        openActionBarOverflowOrOptionsMenu(activityRule.activity)
        clickOn(R.string.options_settings)

        Intents.intended(hasComponent(PrefActivity::class.java.name))
    }

    @Test
    fun executingLogoutGoesBackToLogin() {
        `when`(sharedPref.appStartingScreen).thenReturn(NavigationScreen.Anime)
        activityRule.launchActivity(null)

        clickOn(R.id.menuMainNavigationAnime)
        openActionBarOverflowOrOptionsMenu(activityRule.activity)
        clickOn(R.string.options_log_out)
        onView(withText(android.R.string.yes))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .perform(click())

        intended(hasComponent(LoginActivity::class.java.name))
    }

    private fun withBottomNavItemCheckedStatus(isChecked: Boolean): Matcher<View> {
        return object :
            BoundedMatcher<View, BottomNavigationItemView>(BottomNavigationItemView::class.java) {
            var triedMatching: Boolean = false

            override fun describeTo(description: Description) {
                if (triedMatching) {
                    description.appendText("with BottomNavigationItem check status: $isChecked")
                    description.appendText("But was: ${!isChecked}")
                }
            }

            override fun matchesSafely(item: BottomNavigationItemView): Boolean {
                triedMatching = true
                return item.itemData.isChecked == isChecked
            }
        }
    }
}
