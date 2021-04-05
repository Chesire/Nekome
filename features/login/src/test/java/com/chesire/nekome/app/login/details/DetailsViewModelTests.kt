package com.chesire.nekome.app.login.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.chesire.nekome.core.Resource
import com.chesire.nekome.datasource.auth.AccessTokenRepository
import com.chesire.nekome.datasource.auth.AccessTokenResult
import com.chesire.nekome.datasource.user.UserRepository
import com.chesire.nekome.testing.CoroutinesMainDispatcherRule
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailsViewModelTests {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = CoroutinesMainDispatcherRule()

    @Test
    fun `empty username produces LoginStatus#EmptyUsername`() {
        val mockAccessTokenRepo = mockk<AccessTokenRepository>()
        val mockRepo = mockk<UserRepository>()
        val mockObserver = mockk<Observer<LoginStatus>> {
            every { onChanged(any()) } just Runs
        }

        DetailsViewModel(mockAccessTokenRepo, mockRepo)
            .run {
                loginStatus.observeForever(mockObserver)
                login("", "password")
            }

        verify { mockObserver.onChanged(LoginStatus.EmptyUsername) }
    }

    @Test
    fun `empty password produces LoginStatus#EmptyPassword`() {
        val mockAccessTokenRepo = mockk<AccessTokenRepository>()
        val mockRepo = mockk<UserRepository>()
        val mockObserver = mockk<Observer<LoginStatus>> {
            every { onChanged(any()) } just Runs
        }

        DetailsViewModel(mockAccessTokenRepo, mockRepo)
            .run {
                loginStatus.observeForever(mockObserver)
                login("username", "")
            }

        verify { mockObserver.onChanged(LoginStatus.EmptyPassword) }
    }

    @Test
    fun `login failure 401 produces LoginStatus#InvalidCredentials`() {
        val mockAccessTokenRepo = mockk<AccessTokenRepository> {
            coEvery { login(any(), any()) } returns AccessTokenResult.InvalidCredentials
        }
        val mockRepo = mockk<UserRepository>()
        val mockObserver = mockk<Observer<LoginStatus>> {
            every { onChanged(any()) } just Runs
        }

        DetailsViewModel(mockAccessTokenRepo, mockRepo)
            .run {
                loginStatus.observeForever(mockObserver)
                login("username", "password")
            }

        verify { mockObserver.onChanged(LoginStatus.InvalidCredentials) }
    }

    @Test
    fun `login failure produces LoginStatus#Error`() {
        val mockAccessTokenRepo = mockk<AccessTokenRepository> {
            coEvery { login(any(), any()) } returns AccessTokenResult.CommunicationError
        }
        val mockRepo = mockk<UserRepository>()
        val mockObserver = mockk<Observer<LoginStatus>> {
            every { onChanged(any()) } just Runs
        }

        DetailsViewModel(mockAccessTokenRepo, mockRepo)
            .run {
                loginStatus.observeForever(mockObserver)
                login("username", "password")
            }

        verify { mockObserver.onChanged(LoginStatus.Error) }
    }

    @Test
    fun `login success begins to getUser`() {
        val mockAccessTokenRepo = mockk<AccessTokenRepository> {
            coEvery { login(any(), any()) } returns AccessTokenResult.Success
            coEvery { clear() } just Runs
        }
        val mockRepo = mockk<UserRepository> {
            coEvery {
                refreshUser()
            } coAnswers {
                Resource.Error("", 0)
            }
        }

        DetailsViewModel(mockAccessTokenRepo, mockRepo)
            .run {
                login("username", "password")
            }

        coVerify { mockRepo.refreshUser() }
    }

    @Test
    fun `getUser success produces LoginStatus#Success`() {
        val mockAccessTokenRepo = mockk<AccessTokenRepository> {
            coEvery { login(any(), any()) } returns AccessTokenResult.Success
        }
        val mockRepo = mockk<UserRepository> {
            coEvery { refreshUser() } coAnswers { Resource.Success(Unit) }
        }
        val mockObserver = mockk<Observer<LoginStatus>> {
            every { onChanged(any()) } just Runs
        }

        DetailsViewModel(mockAccessTokenRepo, mockRepo)
            .run {
                loginStatus.observeForever(mockObserver)
                login("username", "password")
            }

        verify { mockObserver.onChanged(LoginStatus.Success) }
    }

    @Test
    fun `getUser failure clears stored auth`() {
        val mockAccessTokenRepo = mockk<AccessTokenRepository> {
            coEvery { login(any(), any()) } returns AccessTokenResult.Success
            coEvery { clear() } just Runs
        }
        val mockRepo = mockk<UserRepository> {
            coEvery { refreshUser() } coAnswers { Resource.Error("") }
        }
        val mockObserver = mockk<Observer<LoginStatus>> {
            every { onChanged(any()) } just Runs
        }

        DetailsViewModel(mockAccessTokenRepo, mockRepo)
            .run {
                loginStatus.observeForever(mockObserver)
                login("username", "password")
            }

        coVerify { mockAccessTokenRepo.clear() }
    }

    @Test
    fun `getUser failure produces LoginStatus#Error`() {
        val mockAccessTokenRepo = mockk<AccessTokenRepository> {
            coEvery { login(any(), any()) } returns AccessTokenResult.Success
            coEvery { clear() } just Runs
        }
        val mockRepo = mockk<UserRepository> {
            coEvery { refreshUser() } coAnswers { Resource.Error("") }
        }
        val mockObserver = mockk<Observer<LoginStatus>> {
            every { onChanged(any()) } just Runs
        }

        DetailsViewModel(mockAccessTokenRepo, mockRepo)
            .run {
                loginStatus.observeForever(mockObserver)
                login("username", "password")
            }

        verify { mockObserver.onChanged(LoginStatus.Error) }
    }
}
