package com.chesire.malime.flow.login.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.chesire.malime.core.Resource
import com.chesire.malime.core.api.AuthApi
import com.chesire.malime.core.models.UserModel
import com.chesire.malime.repo.UserRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
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
class DetailsViewModelTests {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val testDispatcher = Dispatchers.Unconfined

    @Test
    fun `empty username produces LoginStatus#EmptyUsername`() = runBlocking {
        val mockAuth = mockk<AuthApi>()
        val mockRepo = mockk<UserRepository>()
        val mockObserver = mockk<Observer<LoginStatus>> {
            every { onChanged(any()) } just Runs
        }

        DetailsViewModel(mockAuth, mockRepo, testDispatcher).run {
            username.value = ""
            password.value = "password"
            loginStatus.observeForever(mockObserver)
            login()
        }

        verify { mockObserver.onChanged(LoginStatus.EmptyUsername) }
    }

    @Test
    fun `empty password produces LoginStatus#EmptyPassword`() = runBlocking {
        val mockAuth = mockk<AuthApi>()
        val mockRepo = mockk<UserRepository>()
        val mockObserver = mockk<Observer<LoginStatus>> {
            every { onChanged(any()) } just Runs
        }

        DetailsViewModel(mockAuth, mockRepo, testDispatcher).run {
            username.value = "username"
            password.value = ""
            loginStatus.observeForever(mockObserver)
            login()
        }

        verify { mockObserver.onChanged(LoginStatus.EmptyPassword) }
    }

    @Test
    fun `login failure produces LoginStatus#Error`() = runBlocking {
        val mockAuth = mockk<AuthApi> {
            coEvery { login(any(), any()) } coAnswers {
                Resource.Error("", 0)
            }
        }
        val mockRepo = mockk<UserRepository>()
        val mockObserver = mockk<Observer<LoginStatus>> {
            every { onChanged(any()) } just Runs
        }

        DetailsViewModel(mockAuth, mockRepo, testDispatcher).run {
            username.value = "username"
            password.value = "password"
            loginStatus.observeForever(mockObserver)
            login()
        }

        verify { mockObserver.onChanged(LoginStatus.Error) }
    }

    @Test
    fun `login success begins to getUser`() = runBlocking {
        val mockAuth = mockk<AuthApi> {
            coEvery { login(any(), any()) } coAnswers {
                Resource.Success(Any())
            }
            coEvery { clearAuth() } coAnswers { }
        }
        val mockRepo = mockk<UserRepository> {
            coEvery { refreshUser() } coAnswers {
                Resource.Error("", 0)
            }
        }

        DetailsViewModel(mockAuth, mockRepo, testDispatcher).run {
            username.value = "username"
            password.value = "password"
            login()
        }

        coVerify { mockRepo.refreshUser() }
    }

    @Test
    fun `getUser success produces LoginStatus#Success`() = runBlocking {
        val expectedModel = mockk<UserModel>()
        val mockAuth = mockk<AuthApi> {
            coEvery { login(any(), any()) } coAnswers {
                Resource.Success(Any())
            }
        }
        val mockRepo = mockk<UserRepository> {
            coEvery { refreshUser() } coAnswers { Resource.Success(expectedModel) }
        }
        val mockObserver = mockk<Observer<LoginStatus>> {
            every { onChanged(any()) } just Runs
        }

        DetailsViewModel(mockAuth, mockRepo, testDispatcher).run {
            username.value = "username"
            password.value = "password"
            loginStatus.observeForever(mockObserver)
            login()
        }

        verify { mockObserver.onChanged(LoginStatus.Success) }
    }

    @Test
    fun `getUser failure clears stored auth`() = runBlocking {
        val mockAuth = mockk<AuthApi> {
            coEvery { login(any(), any()) } coAnswers {
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

        DetailsViewModel(mockAuth, mockRepo, testDispatcher).run {
            username.value = "username"
            password.value = "password"
            loginStatus.observeForever(mockObserver)
            login()
        }

        coVerify { mockAuth.clearAuth() }
    }

    @Test
    fun `getUser failure produces LoginStatus#Error`() = runBlocking {
        val mockAuth = mockk<AuthApi> {
            coEvery { login(any(), any()) } coAnswers {
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

        DetailsViewModel(mockAuth, mockRepo, testDispatcher).run {
            username.value = "username"
            password.value = "password"
            loginStatus.observeForever(mockObserver)
            login()
        }

        verify { mockObserver.onChanged(LoginStatus.Error) }
    }
}
