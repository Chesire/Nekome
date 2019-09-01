package com.chesire.malime.flow.login.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.chesire.malime.core.models.UserModel
import com.chesire.malime.login.CoroutinesMainDispatcherRule
import com.chesire.malime.server.Resource
import com.chesire.malime.server.api.AuthApi
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
        val mockRepo = mockk<com.chesire.malime.account.UserRepository>()
        val mockObserver = mockk<Observer<com.chesire.malime.flow.login.details.LoginStatus>> {
            every { onChanged(any()) } just Runs
        }

        com.chesire.malime.flow.login.details.DetailsViewModel(mockAuth, mockRepo).run {
            username.value = ""
            password.value = "password"
            loginStatus.observeForever(mockObserver)
            login()
        }

        verify { mockObserver.onChanged(com.chesire.malime.flow.login.details.LoginStatus.EmptyUsername) }
    }

    @Test
    fun `empty password produces LoginStatus#EmptyPassword`() {
        val mockAuth = mockk<AuthApi>()
        val mockRepo = mockk<com.chesire.malime.account.UserRepository>()
        val mockObserver = mockk<Observer<com.chesire.malime.flow.login.details.LoginStatus>> {
            every { onChanged(any()) } just Runs
        }

        com.chesire.malime.flow.login.details.DetailsViewModel(mockAuth, mockRepo).run {
            username.value = "username"
            password.value = ""
            loginStatus.observeForever(mockObserver)
            login()
        }

        verify { mockObserver.onChanged(com.chesire.malime.flow.login.details.LoginStatus.EmptyPassword) }
    }

    @Test
    fun `login failure 401 produces LoginStatus#InvalidCredentials`() {
        val mockAuth = mockk<AuthApi> {
            coEvery { login(any(), any()) } coAnswers {
                Resource.Error("", 401)
            }
        }
        val mockRepo = mockk<com.chesire.malime.account.UserRepository>()
        val mockObserver = mockk<Observer<com.chesire.malime.flow.login.details.LoginStatus>> {
            every { onChanged(any()) } just Runs
        }

        com.chesire.malime.flow.login.details.DetailsViewModel(mockAuth, mockRepo).run {
            username.value = "username"
            password.value = "password"
            loginStatus.observeForever(mockObserver)
            login()
        }

        verify { mockObserver.onChanged(com.chesire.malime.flow.login.details.LoginStatus.InvalidCredentials) }
    }

    @Test
    fun `login failure produces LoginStatus#Error`() {
        val mockAuth = mockk<AuthApi> {
            coEvery { login(any(), any()) } coAnswers {
                Resource.Error("", 0)
            }
        }
        val mockRepo = mockk<com.chesire.malime.account.UserRepository>()
        val mockObserver = mockk<Observer<com.chesire.malime.flow.login.details.LoginStatus>> {
            every { onChanged(any()) } just Runs
        }

        com.chesire.malime.flow.login.details.DetailsViewModel(mockAuth, mockRepo).run {
            username.value = "username"
            password.value = "password"
            loginStatus.observeForever(mockObserver)
            login()
        }

        verify { mockObserver.onChanged(com.chesire.malime.flow.login.details.LoginStatus.Error) }
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
        val mockRepo = mockk<com.chesire.malime.account.UserRepository> {
            coEvery {
                refreshUser()
            } coAnswers {
                Resource.Error("", 0)
            }
        }

        com.chesire.malime.flow.login.details.DetailsViewModel(mockAuth, mockRepo).run {
            username.value = "username"
            password.value = "password"
            login()
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
        val mockRepo = mockk<com.chesire.malime.account.UserRepository> {
            coEvery { refreshUser() } coAnswers { Resource.Success(expectedModel) }
        }
        val mockObserver = mockk<Observer<com.chesire.malime.flow.login.details.LoginStatus>> {
            every { onChanged(any()) } just Runs
        }

        com.chesire.malime.flow.login.details.DetailsViewModel(mockAuth, mockRepo).run {
            username.value = "username"
            password.value = "password"
            loginStatus.observeForever(mockObserver)
            login()
        }

        verify { mockObserver.onChanged(com.chesire.malime.flow.login.details.LoginStatus.Success) }
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
        val mockRepo = mockk<com.chesire.malime.account.UserRepository> {
            coEvery { refreshUser() } coAnswers { Resource.Error("") }
        }
        val mockObserver = mockk<Observer<com.chesire.malime.flow.login.details.LoginStatus>> {
            every { onChanged(any()) } just Runs
        }

        com.chesire.malime.flow.login.details.DetailsViewModel(mockAuth, mockRepo).run {
            username.value = "username"
            password.value = "password"
            loginStatus.observeForever(mockObserver)
            login()
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
        val mockRepo = mockk<com.chesire.malime.account.UserRepository> {
            coEvery { refreshUser() } coAnswers { Resource.Error("") }
        }
        val mockObserver = mockk<Observer<com.chesire.malime.flow.login.details.LoginStatus>> {
            every { onChanged(any()) } just Runs
        }

        com.chesire.malime.flow.login.details.DetailsViewModel(mockAuth, mockRepo).run {
            username.value = "username"
            password.value = "password"
            loginStatus.observeForever(mockObserver)
            login()
        }

        verify { mockObserver.onChanged(com.chesire.malime.flow.login.details.LoginStatus.Error) }
    }
}
