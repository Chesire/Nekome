package com.chesire.malime.flow.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.chesire.malime.core.Resource
import com.chesire.malime.core.api.AuthApi
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LoginViewModelTests {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun `empty username produces error`() = runBlocking {
        val mockAuth = mockk<AuthApi>()
        val mockObserver = mockk<Observer<LoginViewModel.LoginStatus>> {
            every { onChanged(any()) } just Runs
        }

        with(LoginViewModel(mockAuth, Dispatchers.Unconfined)) {
            username.value = ""
            password.value = "password"
            loginStatus.observeForever(mockObserver)

            login()
        }

        verify { mockObserver.onChanged(LoginViewModel.LoginStatus.EmptyUsername) }
    }

    @Test
    fun `empty password produces error`() = runBlocking {
        val mockAuth = mockk<AuthApi>()
        val mockObserver = mockk<Observer<LoginViewModel.LoginStatus>> {
            every { onChanged(any()) } just Runs
        }

        with(LoginViewModel(mockAuth, Dispatchers.Unconfined)) {
            username.value = "username"
            password.value = ""
            loginStatus.observeForever(mockObserver)

            login()
        }

        verify { mockObserver.onChanged(LoginViewModel.LoginStatus.EmptyPassword) }
    }

    @Test
    fun `login failure produces error`() = runBlocking {
        val mockAuth = mockk<AuthApi> {
            coEvery { login(any(), any()) } coAnswers {
                Resource.Error("", 0)
            }
        }
        val mockObserver = mockk<Observer<LoginViewModel.LoginStatus>> {
            every { onChanged(any()) } just Runs
        }

        with(LoginViewModel(mockAuth, Dispatchers.Unconfined)) {
            username.value = "username"
            password.value = "password"
            loginStatus.observeForever(mockObserver)

            login()
        }

        verify { mockObserver.onChanged(LoginViewModel.LoginStatus.Error) }
    }

    @Test
    fun `login success produces success`() = runBlocking {
        val mockAuth = mockk<AuthApi> {
            coEvery { login(any(), any()) } coAnswers {
                Resource.Success(Any())
            }
        }
        val mockObserver = mockk<Observer<LoginViewModel.LoginStatus>> {
            every { onChanged(any()) } just Runs
        }

        with(LoginViewModel(mockAuth, Dispatchers.Unconfined)) {
            username.value = "username"
            password.value = "password"
            loginStatus.observeForever(mockObserver)

            login()
        }

        verify { mockObserver.onChanged(LoginViewModel.LoginStatus.Success) }
    }
}
