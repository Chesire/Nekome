package com.chesire.nekome.robots.login

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.chesire.nekome.app.login.credentials.ui.CredentialsTags

/**
 * Method to interact with the [LoginCredentialsRobot].
 */
fun loginCredentials(
    composeContentTestRule: ComposeContentTestRule,
    func: LoginCredentialsRobot.() -> Unit
) = LoginCredentialsRobot(composeContentTestRule).apply(func)

/**
 * Robot to interact with the login credentials screen.
 */
class LoginCredentialsRobot(private val composeContentTestRule: ComposeContentTestRule) {

    /**
     * Enters the username.
     */
    fun enterUsername(username: String) {
        composeContentTestRule
            .onNodeWithTag(CredentialsTags.Username)
            .performTextInput(username)
    }

    /**
     * Enters the password.
     */
    fun enterPassword(password: String) {
        composeContentTestRule
            .onNodeWithTag(CredentialsTags.Password)
            .performTextInput(password)
    }

    /**
     * Clicks the login button.
     */
    fun clickLogin() {
        composeContentTestRule
            .onNodeWithText("Login")
            .performClick()
    }

    /**
     * Clicks the forgot password button.
     */
    fun clickForgotPassword() {
        composeContentTestRule
            .onNodeWithText("Forgot Password?")
            .performClick()
    }

    /**
     * Clicks the sign up button.
     */
    fun clickSignUp() {
        composeContentTestRule
            .onNodeWithText("No account? Sign up with Kitsu")
            .performClick()
    }

    /**
     * Executes validation steps.
     */
    infix fun validate(func: LoginCredentialsResultRobot.() -> Unit) =
        LoginCredentialsResultRobot(composeContentTestRule).apply { func() }
}

/**
 * Robot to check the results for the login details screen.
 */
class LoginCredentialsResultRobot(private val composeContentTestRule: ComposeContentTestRule) {

    /**
     * Asserts the login credentials screen is shown.
     */
    fun isVisible() {
        composeContentTestRule
            .onNodeWithTag(CredentialsTags.Root)
            .assertIsDisplayed()
    }

    /**
     * Asserts the error for an empty email field.
     */
    fun isEmptyEmailError() {
        composeContentTestRule
            .onNodeWithTag(CredentialsTags.Username)
        // TODO: Verify it is in error state
    }

    /**
     * Asserts the error for an empty password field.
     */
    fun isEmptyPasswordError() {
        composeContentTestRule
            .onNodeWithTag(CredentialsTags.Password)
        // TODO: Verify it is in error state
    }

    /**
     * Asserts the error for having invalid credentials.
     */
    fun isInvalidCredentialsError() {
        composeContentTestRule
            .onNodeWithTag(CredentialsTags.Snackbar)
            .assertIsDisplayed()
            .onChild()
            .onChild()
            .onChild()
            .assertTextContains("invalid credentials provided", ignoreCase = true, substring = true)
    }

    /**
     * Asserts a generic network error.
     */
    fun isGenericError() {
        composeContentTestRule
            .onNodeWithTag(CredentialsTags.Snackbar)
            .assertIsDisplayed()
            .onChild()
            .onChild()
            .onChild()
            .assertTextContains("please try again", ignoreCase = true, substring = true)
    }
}
