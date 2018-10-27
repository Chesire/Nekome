package com.chesire.malime.view.login.mal

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.chesire.malime.R
import com.chesire.malime.core.models.AuthModel
import com.chesire.malime.mal.api.MalAuthorizer
import com.chesire.malime.mal.api.MalManager
import com.chesire.malime.util.SharedPref
import com.chesire.malime.view.login.LoginStatus
import com.nhaarman.mockitokotlin2.mock
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

class MalLoginViewModelTests {
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private lateinit var testObject: MalLoginViewModel
    private val sharedPref = mock<SharedPref> { }
    private val malManager = mock<MalManager> { }
    private val authorizer = mock<MalAuthorizer> { }
    private val errorObserver = mock<Observer<Int>> { }
    private val loginObserver = mock<Observer<LoginStatus>> { }
    private val testScheduler = TestScheduler()

    @Before
    fun setup() {
        testObject = MalLoginViewModel(sharedPref, malManager, authorizer)
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
    fun `empty username provides an error message`() {
        testObject.loginModel.userName = ""

        testObject.executeLogin("dummyString")

        verify(errorObserver).onChanged(R.string.login_failure_username)
        verify(malManager, never()).login(
            testObject.loginModel.userName,
            testObject.loginModel.password
        )
    }

    @Test
    fun `empty password provides an error message`() {
        testObject.loginModel.password = ""

        testObject.executeLogin("dummyString")

        verify(errorObserver).onChanged(R.string.login_failure_password)
        verify(malManager, never()).login(
            testObject.loginModel.userName,
            testObject.loginModel.password
        )
    }

    @Test
    fun `empty b64encoded credentials provides an error message`() {
        testObject.executeLogin("")

        verify(errorObserver).onChanged(R.string.login_failure)
        verify(malManager, never()).login(
            testObject.loginModel.userName,
            testObject.loginModel.password
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
        val returnedModel = AuthModel("authtoken", "refresh", 0, "provider")

        `when`(
            malManager.login(
                testObject.loginModel.userName,
                testObject.loginModel.password
            )
        ).thenReturn(Single.just(returnedModel))

        testObject.executeLogin("dummyString")
        testScheduler.triggerActions()

        verify(authorizer).storeAuthDetails(returnedModel)
        verify(authorizer).storeUser(testObject.loginModel.userName)
    }

    @Test
    fun `successful login calls loginResponse with LoginStatus#SUCCESS`() {
        `when`(
            malManager.login(
                testObject.loginModel.userName,
                testObject.loginModel.password
            )
        ).thenReturn(Single.just(AuthModel("", "", 0, "provider")))

        testObject.executeLogin("dummyString")
        testScheduler.triggerActions()

        verify(loginObserver).onChanged(LoginStatus.PROCESSING)
        verify(loginObserver).onChanged(LoginStatus.SUCCESS)
        verify(loginObserver).onChanged(LoginStatus.FINISHED)
    }
}
