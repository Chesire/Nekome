package com.chesire.nekome.robots.login

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.chesire.nekome.R
import com.chesire.nekome.helpers.ToastMatcher.Companion.onToast
import com.chesire.nekome.helpers.clickClickableSpan
import com.schibsted.spain.barista.assertion.BaristaErrorAssertions.assertError
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaEditTextInteractions.writeTo

/**
 * Method to interact with the [LoginDetailsRobot].
 */
fun loginDetails(func: LoginDetailsRobot.() -> Unit) = LoginDetailsRobot().apply { func() }

/**
 * Robot to interact with the login details screen.
 */
class LoginDetailsRobot {

    /**
     * Enters the username.
     */
    fun enterUsername(username: String) {
        writeTo(R.id.usernameText, username)
    }

    /**
     * Enters the password.
     */
    fun enterPassword(password: String) {
        writeTo(R.id.passwordText, password)
    }

    /**
     * Clicks the login button.
     */
    fun clickLogin() {
        clickOn(R.id.loginButton)
    }

    /**
     * Clicks the forgot password button.
     */
    fun clickForgotPassword() {
        clickOn(R.id.forgotPasswordButton)
    }

    /**
     * Clicks the sign up button.
     */
    fun clickSignUp() {
        onView(withId(R.id.signUp)).perform(clickClickableSpan(R.string.login_sign_up_link_target))
    }

    /**
     * Executes validation steps.
     */
    infix fun validate(func: LoginDetailsResultRobot.() -> Unit): LoginDetailsResultRobot {
        return LoginDetailsResultRobot().apply { func() }
    }
}

/**
 * Robot to check the results for the login details screen.
 */
class LoginDetailsResultRobot {

    /**
     * Asserts the error for an empty email field.
     */
    fun isEmptyEmailError() {
        assertError(R.id.usernameLayout, R.string.login_error_empty_username)
    }

    /**
     * Asserts the error for an empty password field.
     */
    fun isEmptyPasswordError() {
        assertError(R.id.passwordLayout, R.string.login_error_empty_password)
    }

    /**
     * Asserts the error for having invalid credentials.
     */
    fun isInvalidCredentialsError() {
        onToast(R.string.login_error_credentials).check(matches(isDisplayed()))
    }

    /**
     * Asserts a generic network error.
     */
    fun isGenericError() {
        onToast(R.string.login_error_generic).check(matches(isDisplayed()))
    }
}
