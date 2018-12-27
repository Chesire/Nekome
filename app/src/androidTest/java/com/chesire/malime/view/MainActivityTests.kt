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
import com.chesire.malime.view.login.LoginActivity
import com.chesire.malime.view.preferences.PrefActivity
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock

@RunWith(AndroidJUnit4::class)
class MainActivityTests {
    @get:Rule
    var daggerRule = espressoDaggerMockRule()

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java, false, false)

    @Mock
    lateinit var authorization: Authorization

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun canNavigateToAnimeView() {
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
        assertDisplayed(R.id.maldisplay_layout)
    }

    @Test
    fun canNavigateToMangaView() {
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
        assertDisplayed(R.id.maldisplay_layout)
    }

    @Test
    fun canNavigateToSearchView() {
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
        assertDisplayed(R.id.search_layout)
    }

    @Test
    @Ignore("Couldn't find anything that says how this could be tested, will need to at some point")
    fun canLaunchProfile() {
    }

    @Test
    @Ignore("Settings is no longer displayed")
    fun canLaunchPreferencesFromMalDisplay() {
        activityRule.launchActivity(null)
        clickOn(R.id.menuMainNavigationAnime)
        openActionBarOverflowOrOptionsMenu(activityRule.activity)
        clickOn(R.string.options_settings)

        Intents.intended(hasComponent(PrefActivity::class.java.name))
    }

    @Test
    @Ignore("Settings is no longer displayed")
    fun canLaunchPreferencesFromSearch() {
        activityRule.launchActivity(null)
        clickOn(R.id.menuMainNavigationSearch)
        openActionBarOverflowOrOptionsMenu(activityRule.activity)
        clickOn(R.string.options_settings)

        Intents.intended(hasComponent(PrefActivity::class.java.name))
    }

    @Test
    fun executingLogoutGoesBackToLogin() {
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
