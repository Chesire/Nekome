package com.chesire.malime.view

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.chesire.malime.INVALID_PASSWORD
import com.chesire.malime.INVALID_USERNAME
import com.chesire.malime.R
import com.chesire.malime.VALID_PASSWORD
import com.chesire.malime.VALID_USERNAME
import com.chesire.malime.core.api.AuthApi
import com.chesire.malime.core.models.AuthModel
import com.chesire.malime.injection.espressoDaggerMockRule
import com.chesire.malime.view.login.LoginActivity
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaSleepInteractions.sleep
import io.reactivex.Single
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`

@RunWith(AndroidJUnit4::class)
class LoginTests {
    @get:Rule
    var daggerRule = espressoDaggerMockRule()

    @get:Rule
    val activityRule = ActivityTestRule(LoginActivity::class.java, false, false)

    @Mock
    private lateinit var auth: AuthApi

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    @Ignore("Testing toasts can be hit or miss, the toast should be mocked. For now ignoring")
    fun displayErrorForBlankUsername() {
        activityRule.launchActivity(null)
        onView(withId(R.id.fragmentKitsuLoginUsernameEditText)).perform(clearText())
        onView(withId(R.id.fragmentKitsuLoginPasswordEditText)).perform(
            typeText(VALID_PASSWORD),
            closeSoftKeyboard()
        )

        clickOn(R.id.fragmentKitsuLoginButton)

        onView(withText(R.string.login_failure_email))
            .inRoot(withDecorView(not(activityRule.activity.window.decorView)))
            .check(matches(isDisplayed()))
    }

    @Test
    @Ignore("Testing toasts can be hit or miss, the toast should be mocked. For now ignoring")
    fun displayErrorForBlankPassword() {
        activityRule.launchActivity(null)
        onView(withId(R.id.fragmentKitsuLoginUsernameEditText)).perform(typeText(VALID_USERNAME))
        onView(withId(R.id.fragmentKitsuLoginPasswordEditText)).perform(
            clearText(),
            closeSoftKeyboard()
        )

        clickOn(R.id.fragmentKitsuLoginButton)

        onView(withText(R.string.login_failure_password))
            .inRoot(withDecorView(not(activityRule.activity.window.decorView)))
            .check(matches(isDisplayed()))
    }

    @Test
    @Ignore("Testing toasts can be hit or miss, the toast should be mocked. For now ignoring")
    fun displayErrorForInvalidUsername() {
        `when`(
            auth.login(
                INVALID_USERNAME,
                VALID_PASSWORD
            )
        ).thenReturn(Single.error { Throwable("displayErrorForInvalidUsername") })

        activityRule.launchActivity(null)

        onView(withId(R.id.fragmentKitsuLoginUsernameEditText)).perform(typeText(INVALID_USERNAME))
        onView(withId(R.id.fragmentKitsuLoginPasswordEditText)).perform(
            typeText(VALID_PASSWORD),
            closeSoftKeyboard()
        )

        clickOn(R.id.fragmentKitsuLoginButton)

        onView(withText(R.string.login_failure))
            .inRoot(withDecorView(not(activityRule.activity.window.decorView)))
            .check(matches(isDisplayed()))
    }

    @Test
    @Ignore("Testing toasts can be hit or miss, the toast should be mocked. For now ignoring")
    fun displayErrorForInvalidPassword() {
        `when`(
            auth.login(
                VALID_USERNAME,
                INVALID_PASSWORD
            )
        ).thenReturn(Single.error { Throwable("displayErrorForInvalidPassword") })

        activityRule.launchActivity(null)

        onView(withId(R.id.fragmentKitsuLoginUsernameEditText)).perform(typeText(VALID_USERNAME))
        onView(withId(R.id.fragmentKitsuLoginPasswordEditText)).perform(
            typeText(INVALID_PASSWORD),
            closeSoftKeyboard()
        )

        clickOn(R.id.fragmentKitsuLoginButton)

        onView(withText(R.string.login_failure))
            .inRoot(withDecorView(not(activityRule.activity.window.decorView)))
            .check(matches(isDisplayed()))
    }

    @Test
    @Ignore("Testing toasts can be hit or miss, the toast should be mocked. For now ignoring")
    fun canNavigateFieldsWithIMEButton() {
        activityRule.launchActivity(null)
        onView(withId(R.id.fragmentKitsuLoginUsernameEditText)).perform(
            clearText(),
            pressImeActionButton()
        )
        onView(withId(R.id.fragmentKitsuLoginPasswordEditText)).perform(
            clearText(),
            pressImeActionButton()
        )

        onView(withText(R.string.login_failure_email))
            .inRoot(withDecorView(not(activityRule.activity.window.decorView)))
            .check(matches(isDisplayed()))
    }

    @Test
    fun correctDetailsLaunchesMainActivity() {
        `when`(
            auth.login(
                VALID_USERNAME,
                VALID_PASSWORD
            )
        ).thenReturn(Single.just(AuthModel("", "", 0, "")))
        `when`(auth.getUserId()).thenReturn(Single.just(1))

        activityRule.launchActivity(null)

        onView(withId(R.id.fragmentKitsuLoginUsernameEditText)).perform(
            typeText(VALID_USERNAME),
            pressImeActionButton()
        )
        onView(withId(R.id.fragmentKitsuLoginPasswordEditText)).perform(
            typeText(VALID_PASSWORD),
            pressImeActionButton()
        )

        sleep(300)
        intended(hasComponent(MainActivity::class.java.name))
    }

    @Test
    @Ignore("Couldn't find anything that says how this could be tested, will need to at some point")
    fun pressingTheCreateAccountLinkOpensPage() {
    }
}
