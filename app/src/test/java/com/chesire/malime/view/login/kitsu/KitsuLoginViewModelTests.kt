package com.chesire.malime.view.login.kitsu

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import android.test.mock.MockApplication
import com.chesire.malime.R
import com.chesire.malime.customMock
import com.chesire.malime.kitsu.api.KitsuManagerFactory
import com.chesire.malime.kitsu.api.KitsuManager
import com.chesire.malime.util.SharedPref
import com.chesire.malime.core.flags.SupportedService
import com.chesire.malime.view.login.LoginStatus
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
        testObject.loginModel.email = "email"
        testObject.loginModel.password = "password"

        `when`(kitsuManagerFactory.get()).thenReturn(kitsuManager)
        `when`(sharedPref.putPrimaryService(SupportedService.Kitsu)).thenReturn(sharedPref)
        `when`(sharedPref.putUserId(ArgumentMatchers.anyInt())).thenReturn(sharedPref)
    }

    @After
    fun teardown() {
        testObject.errorResponse.removeObserver(errorObserver)
        testObject.loginResponse.removeObserver(loginObserver)
    }

    @Test
    fun `empty username provides an error message`() {
        testObject.loginModel.userName = ""

        testObject.executeLogin()

        verify(errorObserver).onChanged(R.string.login_failure_display_name)
        verify(kitsuManagerFactory, Times(0)).get()
    }

    @Test
    fun `empty email provides an error message`() {
        testObject.loginModel.email = ""

        testObject.executeLogin()

        verify(errorObserver).onChanged(R.string.login_failure_email)
        verify(kitsuManagerFactory, Times(0)).get()
    }

    @Test
    fun `empty password provides an error message`() {
        testObject.loginModel.password = ""

        testObject.executeLogin()

        verify(errorObserver).onChanged(R.string.login_failure_password)
        verify(kitsuManagerFactory, Times(0)).get()
    }

    /*
    There are more tests to put in here,
    but having trouble working out how to do them because
    of the .zipWith call, so ignoring for now
    */
}