package com.chesire.malime.view.login.kitsu

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import android.test.mock.MockApplication
import com.chesire.malime.R
import com.chesire.malime.core.flags.SupportedService
import com.chesire.malime.core.models.AuthModel
import com.chesire.malime.customMock
import com.chesire.malime.kitsu.api.KitsuManager
import com.chesire.malime.kitsu.api.KitsuManagerFactory
import com.chesire.malime.util.SharedPref
import com.chesire.malime.view.login.LoginStatus
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.internal.verification.Times

@Suppress("DEPRECATION")
class KitsuLoginViewModelTests {
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private lateinit var testObject: KitsuLoginViewModel
    private val sharedPref: SharedPref = customMock()
    private val kitsuManagerFactory: KitsuManagerFactory = customMock()
    private val kitsuManager: KitsuManager = customMock()
    private val errorObserver: Observer<Int> = customMock()
    private val loginObserver: Observer<LoginStatus> = customMock()
    private val testScheduler = TestScheduler()

    @Before
    fun setup() {
        testObject = KitsuLoginViewModel(
            MockApplication(),
            sharedPref,
            kitsuManagerFactory,
            testScheduler,
            testScheduler
        )
        testObject.errorResponse.observeForever(errorObserver)
        testObject.loginResponse.observeForever(loginObserver)
        testObject.loginModel.userName = "username"
        testObject.loginModel.password = "password"

        `when`(kitsuManagerFactory.get(testObject)).thenReturn(kitsuManager)
        `when`(sharedPref.putPrimaryService(SupportedService.Kitsu)).thenReturn(sharedPref)
        `when`(sharedPref.putUserId(ArgumentMatchers.anyInt())).thenReturn(sharedPref)
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
        verify(kitsuManagerFactory, Times(0)).get(testObject)
    }

    @Test
    fun `empty password provides an error message`() {
        testObject.loginModel.password = ""

        testObject.executeLogin()

        verify(errorObserver).onChanged(R.string.login_failure_password)
        verify(kitsuManagerFactory, Times(0)).get(testObject)
    }

    @Test
    fun `failure to login provides error message`() {
        `when`(
            kitsuManager.login(
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
            kitsuManager.login(
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
    fun `successful login saves login details to shared pref`() {
        val expectedId = 915
        val authToken = "dummyString"
        val returnedModel = AuthModel(authToken, "", 0)

        `when`(
            kitsuManager.login(
                testObject.loginModel.userName,
                testObject.loginModel.password
            )
        ).thenReturn(Single.just(returnedModel))
        `when`(kitsuManager.getUserId()).thenReturn(Single.just(expectedId))

        testObject.executeLogin()
        testScheduler.triggerActions()

        verify(sharedPref).putPrimaryService(SupportedService.Kitsu)
        verify(sharedPref).putUserId(expectedId)
        verify(sharedPref).setAuth(returnedModel)
    }

    @Test
    fun `successful login calls loginResponse with LoginStatus#SUCCESS`() {
        val expectedId = 915
        val authToken = "dummyString"
        val returnedModel = AuthModel(authToken, "", 0)

        `when`(
            kitsuManager.login(
                testObject.loginModel.userName,
                testObject.loginModel.password
            )
        ).thenReturn(Single.just(returnedModel))
        `when`(kitsuManager.getUserId()).thenReturn(Single.just(expectedId))

        testObject.executeLogin()
        testScheduler.triggerActions()

        verify(loginObserver).onChanged(LoginStatus.PROCESSING)
        verify(loginObserver).onChanged(LoginStatus.SUCCESS)
        verify(loginObserver).onChanged(LoginStatus.FINISHED)
    }
}