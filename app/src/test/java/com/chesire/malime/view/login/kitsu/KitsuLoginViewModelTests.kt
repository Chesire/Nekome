package com.chesire.malime.view.login.kitsu

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.chesire.malime.R
import com.chesire.malime.core.api.AuthApi
import com.chesire.malime.core.flags.SupportedService
import com.chesire.malime.core.models.AuthModel
import com.chesire.malime.customMock
import com.chesire.malime.kitsu.api.KitsuAuthorizer
import com.chesire.malime.util.SharedPref
import com.chesire.malime.view.login.LoginStatus
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito.`when`
import org.mockito.Mockito.never
import org.mockito.Mockito.verify

class KitsuLoginViewModelTests {
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private lateinit var testObject: KitsuLoginViewModel
    private val sharedPref: SharedPref = customMock()
    private val auth: AuthApi = customMock()
    private val authorizer: KitsuAuthorizer = customMock()
    private val errorObserver: Observer<Int> = customMock()
    private val loginObserver: Observer<LoginStatus> = customMock()
    private val testScheduler = TestScheduler()

    @Before
    fun setup() {
        testObject = KitsuLoginViewModel(sharedPref, auth, authorizer)
            .apply {
                observeScheduler = testScheduler
                subscribeScheduler = testScheduler
                errorResponse.observeForever(errorObserver)
                loginResponse.observeForever(loginObserver)
                loginModel.userName = "username"
                loginModel.password = "password"
            }
    }

    @After
    fun teardown() {
        testObject.errorResponse.removeObserver(errorObserver)
        testObject.loginResponse.removeObserver(loginObserver)
    }

    @Test
    fun `empty email provides an error message`() {
        testObject.loginModel.userName = ""

        testObject.executeLogin()

        verify(errorObserver).onChanged(R.string.login_failure_email)
        verify(auth, never()).login(
            testObject.loginModel.userName,
            testObject.loginModel.password
        )
    }

    @Test
    fun `empty password provides an error message`() {
        testObject.loginModel.password = ""

        testObject.executeLogin()

        verify(errorObserver).onChanged(R.string.login_failure_password)
        verify(auth, never()).login(
            testObject.loginModel.userName,
            testObject.loginModel.password
        )
    }

    @Test
    fun `failure to login provides error message`() {
        `when`(
            auth.login(
                testObject.loginModel.userName,
                testObject.loginModel.password
            )
        ).thenReturn(Single.error(Exception("Test Exception")))

        testObject.executeLogin()
        testScheduler.triggerActions()

        verify(errorObserver).onChanged(R.string.login_failure)
    }

    @Test
    fun `failure to login calls loginResponse with LoginStatus#ERROR`() {
        `when`(
            auth.login(
                testObject.loginModel.userName,
                testObject.loginModel.password
            )
        ).thenReturn(Single.error(Exception("Test Exception")))

        testObject.executeLogin()
        testScheduler.triggerActions()

        verify(loginObserver).onChanged(LoginStatus.PROCESSING)
        verify(loginObserver).onChanged(LoginStatus.ERROR)
        verify(loginObserver).onChanged(LoginStatus.FINISHED)
    }

    @Test
    fun `successful login but failure to get user id clears auth`() {
        val returnedModel = AuthModel("authtoken", "refresh", 0, "provider")

        `when`(
            auth.login(
                testObject.loginModel.userName,
                testObject.loginModel.password
            )
        ).thenReturn(Single.just(returnedModel))
        `when`(auth.getUserId()).thenReturn(Single.error(Exception("Test Exception")))

        testObject.executeLogin()
        testScheduler.triggerActions()

        verify(authorizer).storeAuthDetails(returnedModel)
        verify(authorizer).clear()
    }

    @Test
    fun `successful login but failure to get user id calls loginResponse with LoginStatus#ERROR`() {
        val returnedModel = AuthModel("authtoken", "refresh", 0, "provider")

        `when`(
            auth.login(
                testObject.loginModel.userName,
                testObject.loginModel.password
            )
        ).thenReturn(Single.just(returnedModel))
        `when`(auth.getUserId()).thenReturn(Single.error(Exception("Test Exception")))

        testObject.executeLogin()
        testScheduler.triggerActions()

        verify(loginObserver).onChanged(LoginStatus.PROCESSING)
        verify(loginObserver).onChanged(LoginStatus.ERROR)
        verify(loginObserver).onChanged(LoginStatus.FINISHED)
    }

    @Test
    fun `successful login saves login details to shared pref`() {
        val expectedId = 915
        val returnedModel = AuthModel("authtoken", "refresh", 0, "provider")

        `when`(
            auth.login(
                testObject.loginModel.userName,
                testObject.loginModel.password
            )
        ).thenReturn(Single.just(returnedModel))
        `when`(auth.getUserId()).thenReturn(Single.just(expectedId))

        testObject.executeLogin()
        testScheduler.triggerActions()

        verify(authorizer).storeAuthDetails(returnedModel)
        verify(sharedPref).putPrimaryService(SupportedService.Kitsu)
        verify(authorizer).storeUser(expectedId)
        verify(authorizer, never()).clear()
    }

    @Test
    fun `successful login calls loginResponse with LoginStatus#SUCCESS`() {
        val expectedId = 915
        val returnedModel = AuthModel("authtoken", "refresh", 0, "provider")

        `when`(
            auth.login(
                testObject.loginModel.userName,
                testObject.loginModel.password
            )
        ).thenReturn(Single.just(returnedModel))
        `when`(auth.getUserId()).thenReturn(Single.just(expectedId))

        testObject.executeLogin()
        testScheduler.triggerActions()

        verify(loginObserver).onChanged(LoginStatus.PROCESSING)
        verify(loginObserver).onChanged(LoginStatus.SUCCESS)
        verify(loginObserver).onChanged(LoginStatus.FINISHED)
    }
}
