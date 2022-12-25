@file:OptIn(ExperimentalCoroutinesApi::class)

package com.chesire.nekome.app.login.credentials.ui

import com.chesire.nekome.app.login.R
import com.chesire.nekome.app.login.credentials.core.ClearCredentialsUseCase
import com.chesire.nekome.app.login.credentials.core.PopulateUserDetailsUseCase
import com.chesire.nekome.app.login.credentials.core.VerifyCredentialsFailure
import com.chesire.nekome.app.login.credentials.core.VerifyCredentialsUseCase
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class CredentialsViewModelTest {

    private val verifyCredentials = mockk<VerifyCredentialsUseCase>()
    private val populateUserDetails = mockk<PopulateUserDetailsUseCase>()
    private val clearCredentials = mockk<ClearCredentialsUseCase>(relaxed = true)
    private lateinit var viewModel: CredentialsViewModel

    @Before
    fun setup() {
        clearAllMocks()
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = CredentialsViewModel(verifyCredentials, populateUserDetails, clearCredentials)
    }

    @Test
    fun `When UsernameChanged, Then updated username is emitted`() {
        val expected = "newUsername"
        assertEquals("", viewModel.uiState.value.username)

        viewModel.execute(ViewAction.UsernameChanged(expected))

        assertEquals(expected, viewModel.uiState.value.username)
    }

    @Test
    fun `When PasswordChanged, Then updated password is emitted`() {
        val expected = "newPassword"
        assertEquals("", viewModel.uiState.value.password)

        viewModel.execute(ViewAction.PasswordChanged(expected))

        assertEquals(expected, viewModel.uiState.value.password)
    }

    @Test
    fun `Given verifying credentials fails, When LoginPressed, Then snackbar error is emitted`() =
        runTest {
            coEvery {
                verifyCredentials(any(), any())
            } returns Err(VerifyCredentialsFailure.InvalidCredentials)

            viewModel.apply {
                execute(ViewAction.UsernameChanged("New Username"))
                execute(ViewAction.PasswordChanged("New Password"))
                execute(ViewAction.LoginPressed)
            }

            assertEquals(
                R.string.login_error_credentials,
                viewModel.uiState.value.errorSnackbarMessage
            )
        }

    @Test
    fun `Given populating user details fails, When LoginPressed, credentials are cleared`() =
        runTest {
            coEvery {
                verifyCredentials(any(), any())
            } returns Ok(Unit)
            coEvery {
                populateUserDetails()
            } returns Err(Unit)

            viewModel.apply {
                execute(ViewAction.UsernameChanged("New Username"))
                execute(ViewAction.PasswordChanged("New Password"))
                execute(ViewAction.LoginPressed)
            }

            verify { clearCredentials() }
        }

    @Test
    fun `Given populating user details fails, When LoginPressed, Then snackbar error is emitted`() =
        runTest {
            coEvery {
                verifyCredentials(any(), any())
            } returns Ok(Unit)
            coEvery {
                populateUserDetails()
            } returns Err(Unit)

            viewModel.apply {
                execute(ViewAction.UsernameChanged("New Username"))
                execute(ViewAction.PasswordChanged("New Password"))
                execute(ViewAction.LoginPressed)
            }

            assertEquals(R.string.login_error_generic, viewModel.uiState.value.errorSnackbarMessage)
        }

    @Test
    fun `When ErrorSnackbarObserved, Then new UIState is emitted`() {
        coEvery {
            verifyCredentials(any(), any())
        } returns Ok(Unit)
        coEvery {
            populateUserDetails()
        } returns Err(Unit)

        viewModel.apply {
            execute(ViewAction.UsernameChanged("New Username"))
            execute(ViewAction.PasswordChanged("New Password"))
            execute(ViewAction.LoginPressed)
        }
        assertEquals(R.string.login_error_generic, viewModel.uiState.value.errorSnackbarMessage)
        viewModel.execute(ViewAction.ErrorSnackbarObserved)

        assertNull(viewModel.uiState.value.errorSnackbarMessage)
    }

    @Test
    fun `Given populating user details succeeds, When LoginPressed, Then navigation event is emitted`() =
        runTest {
            coEvery {
                verifyCredentials(any(), any())
            } returns Ok(Unit)
            coEvery {
                populateUserDetails()
            } returns Ok(Unit)

            viewModel.apply {
                execute(ViewAction.UsernameChanged("New Username"))
                execute(ViewAction.PasswordChanged("New Password"))
                execute(ViewAction.LoginPressed)
            }

            assertEquals(true, viewModel.uiState.value.navigateScreenEvent)
        }

    @Test
    fun `When NavigationObserved, Then new UIState is emitted`() {
        coEvery {
            verifyCredentials(any(), any())
        } returns Ok(Unit)
        coEvery {
            populateUserDetails()
        } returns Ok(Unit)

        viewModel.apply {
            execute(ViewAction.UsernameChanged("New Username"))
            execute(ViewAction.PasswordChanged("New Password"))
            execute(ViewAction.LoginPressed)
        }
        assertEquals(true, viewModel.uiState.value.navigateScreenEvent)
        viewModel.execute(ViewAction.NavigationObserved)

        assertNull(viewModel.uiState.value.navigateScreenEvent)
    }
}
