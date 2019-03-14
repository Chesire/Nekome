package com.chesire.malime.flow.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.chesire.malime.core.Resource
import com.chesire.malime.core.api.AuthApi
import com.chesire.malime.core.models.UserModel
import com.chesire.malime.repo.UserRepository
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
    private val testDispatcher = Dispatchers.Unconfined

    @Test
    fun `empty username produces LoginStatus#EmptyUsername`() = runBlocking {
        val mockAuth = mockk<AuthApi>()
        val mockRepo = mockk<UserRepository>()
        val mockObserver = mockk<Observer<LoginViewModel.LoginStatus>> {
            every { onChanged(any()) } just Runs
        }

        with(LoginViewModel(mockAuth, mockRepo, testDispatcher)) {
            username.value = ""
            password.value = "password"
            loginStatus.observeForever(mockObserver)

            login()
        }

        verify { mockObserver.onChanged(LoginViewModel.LoginStatus.EmptyUsername) }
    }

    @Test
    fun `empty password produces LoginStatus#EmptyPassword`() = runBlocking {
        val mockAuth = mockk<AuthApi>()
        val mockRepo = mockk<UserRepository>()
        val mockObserver = mockk<Observer<LoginViewModel.LoginStatus>> {
            every { onChanged(any()) } just Runs
        }

        with(LoginViewModel(mockAuth, mockRepo, testDispatcher)) {
            username.value = "username"
            password.value = ""
            loginStatus.observeForever(mockObserver)

            login()
        }

        verify { mockObserver.onChanged(LoginViewModel.LoginStatus.EmptyPassword) }
    }

    @Test
    fun `login failure produces LoginStatus#Error`() = runBlocking {
        val mockAuth = mockk<AuthApi> {
            coEvery { login(any(), any()) } coAnswers {
                Resource.Error("", 0)
            }
        }
        val mockRepo = mockk<UserRepository>()
        val mockObserver = mockk<Observer<LoginViewModel.LoginStatus>> {
            every { onChanged(any()) } just Runs
        }

        with(LoginViewModel(mockAuth, mockRepo, testDispatcher)) {
            username.value = "username"
            password.value = "password"
            loginStatus.observeForever(mockObserver)

            login()
        }

        verify { mockObserver.onChanged(LoginViewModel.LoginStatus.Error) }
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
            coEvery { retrieveRemoteUser() } coAnswers {
                Resource.Error("", 0)
            }
        }

        with(LoginViewModel(mockAuth, mockRepo, testDispatcher)) {
            username.value = "username"
            password.value = "password"

            login()
        }

        verify {
            runBlocking {
                mockRepo.retrieveRemoteUser()
            }
        }
    }

    @Test
    fun `getUser success inserts user into dao`() = runBlocking {
        val expectedModel = mockk<UserModel>()
        val mockAuth = mockk<AuthApi> {
            coEvery { login(any(), any()) } coAnswers {
                Resource.Success(Any())
            }
        }
        val mockRepo = mockk<UserRepository> {
            coEvery { retrieveRemoteUser() } coAnswers { Resource.Success(expectedModel) }
        }

        with(LoginViewModel(mockAuth, mockRepo, testDispatcher)) {
            username.value = "username"
            password.value = "password"

            login()
        }

        verify {
            runBlocking {
                mockRepo.insertUser(expectedModel)
            }
        }
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
            coEvery { retrieveRemoteUser() } coAnswers { Resource.Success(expectedModel) }
            coEvery { insertUser(any()) } coAnswers { }
        }
        val mockObserver = mockk<Observer<LoginViewModel.LoginStatus>> {
            every { onChanged(any()) } just Runs
        }

        with(LoginViewModel(mockAuth, mockRepo, testDispatcher)) {
            username.value = "username"
            password.value = "password"
            loginStatus.observeForever(mockObserver)

            login()
        }

        verify { mockObserver.onChanged(LoginViewModel.LoginStatus.Success) }
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
            coEvery { retrieveRemoteUser() } coAnswers { Resource.Error("") }
        }
        val mockObserver = mockk<Observer<LoginViewModel.LoginStatus>> {
            every { onChanged(any()) } just Runs
        }

        with(LoginViewModel(mockAuth, mockRepo, testDispatcher)) {
            username.value = "username"
            password.value = "password"
            loginStatus.observeForever(mockObserver)

            login()
        }

        verify {
            runBlocking {
                mockAuth.clearAuth()
            }
        }
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
            coEvery { retrieveRemoteUser() } coAnswers { Resource.Error("") }
        }
        val mockObserver = mockk<Observer<LoginViewModel.LoginStatus>> {
            every { onChanged(any()) } just Runs
        }

        with(LoginViewModel(mockAuth, mockRepo, testDispatcher)) {
            username.value = "username"
            password.value = "password"
            loginStatus.observeForever(mockObserver)

            login()
        }

        verify { mockObserver.onChanged(LoginViewModel.LoginStatus.Error) }
    }
}
