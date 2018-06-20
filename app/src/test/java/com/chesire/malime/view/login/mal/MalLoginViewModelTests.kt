package com.chesire.malime.view.login.mal

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import android.test.mock.MockApplication
import com.chesire.malime.R
import com.chesire.malime.core.flags.SupportedService
import com.chesire.malime.core.models.AuthModel
import com.chesire.malime.customMock
import com.chesire.malime.mal.api.MalManager
import com.chesire.malime.mal.api.MalManagerFactory
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
class MalLoginViewModelTests {
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private lateinit var testObject: MalLoginViewModel
    private val sharedPref: SharedPref = customMock()
    private val malManagerFactory: MalManagerFactory = customMock()
    private val malManager: MalManager = customMock()
    private val errorObserver: Observer<Int> = customMock()
    private val loginObserver: Observer<LoginStatus> = customMock()
    private val testScheduler = TestScheduler()

    @Before
    fun setup() {
        testObject = MalLoginViewModel(
            MockApplication(),
            sharedPref,
            malManagerFactory,
            testScheduler,
            testScheduler
        )
        testObject.errorResponse.observeForever(errorObserver)
        testObject.loginResponse.observeForever(loginObserver)
        testObject.loginModel.userName = "username"
        testObject.loginModel.password = "password"

        `when`(
            malManagerFactory.get(
                testObject,
                testObject.loginModel.userName
            )
        ).thenReturn(malManager)
        `when`(sharedPref.putPrimaryService(SupportedService.MyAnimeList)).thenReturn(sharedPref)
        `when`(sharedPref.putUsername(ArgumentMatchers.anyString())).thenReturn(sharedPref)
    }

    @After
    fun teardown() {
        testObject.errorResponse.removeObserver(errorObserver)
        testObject.loginResponse.removeObserver(loginObserver)
    }

    @Test
    fun `empty username provides an error message`() {
        testObject.loginModel.userName = ""

        testObject.executeLogin("dummyString")

        verify(errorObserver).onChanged(R.string.login_failure_username)
        verify(malManagerFactory, Times(0)).get(
            testObject,
            testObject.loginModel.userName
        )
    }

    @Test
    fun `empty password provides an error message`() {
        testObject.loginModel.password = ""

        testObject.executeLogin("dummyString")

        verify(errorObserver).onChanged(R.string.login_failure_password)
        verify(malManagerFactory, Times(0)).get(
            testObject,
            testObject.loginModel.userName
        )
    }

    @Test
    fun `empty b64encoded credentials provides an error message`() {
        testObject.executeLogin("")

        verify(errorObserver).onChanged(R.string.login_failure)
        verify(malManagerFactory, Times(0)).get(
            testObject,
            testObject.loginModel.userName
        )
    }

    @Test
    fun `failure to login provides an error message`() {
        `when`(
            malManager.login(
                testObject.loginModel.userName,
                testObject.loginModel.password
            )
        ).thenReturn(Single.error(Exception("Test Exception")))

        testObject.executeLogin("dummyString")
        testScheduler.triggerActions()

        verify(errorObserver).onChanged(R.string.login_failure)
    }

    @Test
    fun `failure to login calls loginResponse with LoginStatus#ERROR`() {
        `when`(
            malManager.login(
                testObject.loginModel.userName,
                testObject.loginModel.password
            )
        ).thenReturn(Single.error(Exception("Test Exception")))

        testObject.executeLogin("dummyString")
        testScheduler.triggerActions()

        verify(loginObserver).onChanged(LoginStatus.PROCESSING)
        verify(loginObserver).onChanged(LoginStatus.ERROR)
        verify(loginObserver).onChanged(LoginStatus.FINISHED)
    }

    @Test
    fun `successful login saves login details to shared pref`() {
        val authToken = "dummyString"
        val returnedModel = AuthModel(authToken, "", 0)
        `when`(
            malManager.login(
                testObject.loginModel.userName,
                testObject.loginModel.password
            )
        ).thenReturn(Single.just(returnedModel))

        testObject.executeLogin(authToken)
        testScheduler.triggerActions()

        verify(sharedPref).putPrimaryService(SupportedService.MyAnimeList)
        verify(sharedPref).putUsername(testObject.loginModel.userName)
        verify(sharedPref).setAuth(returnedModel)
    }

    @Test
    fun `successful login calls loginResponse with LoginStatus#SUCCESS`() {
        `when`(
            malManager.login(
                testObject.loginModel.userName,
                testObject.loginModel.password
            )
        ).thenReturn(Single.just(AuthModel("", "", 0)))

        testObject.executeLogin("dummyString")
        testScheduler.triggerActions()

        verify(loginObserver).onChanged(LoginStatus.PROCESSING)
        verify(loginObserver).onChanged(LoginStatus.SUCCESS)
        verify(loginObserver).onChanged(LoginStatus.FINISHED)
    }
}