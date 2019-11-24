package com.chesire.nekome.flow.login.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.chesire.nekome.account.UserRepository
import com.chesire.nekome.core.models.UserModel
import com.chesire.nekome.server.Resource
import com.chesire.nekome.server.api.AuthApi
import com.chesire.nekome.testing.CoroutinesMainDispatcherRule
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class DetailsViewModelTests {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val coroutineRule = CoroutinesMainDispatcherRule()

    @Test
    fun `empty username produces LoginStatus#EmptyUsername`() {
        val mockAuth = mockk<AuthApi>()
        val mockRepo = mockk<UserRepository>()
        val mockObserver = mockk<Observer<LoginStatus>> {
            every { onChanged(any()) } just Runs
        }

        DetailsViewModel(mockAuth, mockRepo).run {
            loginStatus.observeForever(mockObserver)
            login("", "password")
        }

        verify { mockObserver.onChanged(LoginStatus.EmptyUsername) }
    }

    @Test
    fun `empty password produces LoginStatus#EmptyPassword`() {
        val mockAuth = mockk<AuthApi>()
        val mockRepo = mockk<UserRepository>()
        val mockObserver = mockk<Observer<LoginStatus>> {
            every { onChanged(any()) } just Runs
        }

        DetailsViewModel(mockAuth, mockRepo).run {
            loginStatus.observeForever(mockObserver)
            login("username", "")
        }

        verify { mockObserver.onChanged(LoginStatus.EmptyPassword) }
    }

    @Test
    fun `login failure 401 produces LoginStatus#InvalidCredentials`() {
        val mockAuth = mockk<AuthApi> {
            coEvery { login(any(), any()) } coAnswers {
                Resource.Error("", 401)
            }
        }
        val mockRepo = mockk<UserRepository>()
        val mockObserver = mockk<Observer<LoginStatus>> {
            every { onChanged(any()) } just Runs
        }

        DetailsViewModel(mockAuth, mockRepo).run {
            loginStatus.observeForever(mockObserver)
            login("username", "password")
        }

        verify { mockObserver.onChanged(LoginStatus.InvalidCredentials) }
    }

    @Test
    fun `login failure produces LoginStatus#Error`() {
        val mockAuth = mockk<AuthApi> {
            coEvery { login(any(), any()) } coAnswers {
                Resource.Error("", 0)
            }
        }
        val mockRepo = mockk<UserRepository>()
        val mockObserver = mockk<Observer<LoginStatus>> {
            every { onChanged(any()) } just Runs
        }

        DetailsViewModel(mockAuth, mockRepo).run {
            loginStatus.observeForever(mockObserver)
            login("username", "password")
        }

        verify { mockObserver.onChanged(LoginStatus.Error) }
    }

    @Test
    fun `login success begins to getUser`() {
        val mockAuth = mockk<AuthApi> {
            coEvery {
                login(any(), any())
            } coAnswers {
                Resource.Success(Any())
            }
            coEvery { clearAuth() } just Runs
        }
        val mockRepo = mockk<UserRepository> {
            coEvery {
                refreshUser()
            } coAnswers {
                Resource.Error("", 0)
            }
        }

        DetailsViewModel(mockAuth, mockRepo).run {
            login("username", "password")
        }

        coVerify { mockRepo.refreshUser() }
    }

    @Test
    fun `getUser success produces LoginStatus#Success`() {
        val expectedModel = mockk<UserModel>()
        val mockAuth = mockk<AuthApi> {
            coEvery {
                login(any(), any())
            } coAnswers {
                Resource.Success(Any())
            }
        }
        val mockRepo = mockk<UserRepository> {
            coEvery { refreshUser() } coAnswers { Resource.Success(expectedModel) }
        }
        val mockObserver = mockk<Observer<LoginStatus>> {
            every { onChanged(any()) } just Runs
        }

        DetailsViewModel(mockAuth, mockRepo).run {
            loginStatus.observeForever(mockObserver)
            login("username", "password")
        }

        verify { mockObserver.onChanged(LoginStatus.Success) }
    }

    @Test
    fun `getUser failure clears stored auth`() {
        val mockAuth = mockk<AuthApi> {
            coEvery {
                login(any(), any())
            } coAnswers {
                Resource.Success(Any())
            }
            coEvery { clearAuth() } coAnswers { }
        }
        val mockRepo = mockk<UserRepository> {
            coEvery { refreshUser() } coAnswers { Resource.Error("") }
        }
        val mockObserver = mockk<Observer<LoginStatus>> {
            every { onChanged(any()) } just Runs
        }

        DetailsViewModel(mockAuth, mockRepo).run {
            loginStatus.observeForever(mockObserver)
            login("username", "password")
        }

        coVerify { mockAuth.clearAuth() }
    }

    @Test
    fun `getUser failure produces LoginStatus#Error`() {
        val mockAuth = mockk<AuthApi> {
            coEvery {
                login(any(), any())
            } coAnswers {
                Resource.Success(Any())
            }
            coEvery { clearAuth() } coAnswers { }
        }
        val mockRepo = mockk<UserRepository> {
            coEvery { refreshUser() } coAnswers { Resource.Error("") }
        }
        val mockObserver = mockk<Observer<LoginStatus>> {
            every { onChanged(any()) } just Runs
        }

        DetailsViewModel(mockAuth, mockRepo).run {
            loginStatus.observeForever(mockObserver)
            login("username", "password")
        }

        verify { mockObserver.onChanged(LoginStatus.Error) }
    }
}
