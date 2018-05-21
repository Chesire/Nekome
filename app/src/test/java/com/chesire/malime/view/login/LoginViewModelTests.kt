package com.chesire.malime.view.login

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import android.test.mock.MockApplication
import com.chesire.malime.R
import com.chesire.malime.mock
import com.chesire.malime.util.SharedPref
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito.verify

class LoginViewModelTests {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val sharedPref: SharedPref = mock()
    private val errorObserver: Observer<Int> = mock()

    @Test
    fun `empty username provides an error message`() {
        val testObject = LoginViewModel(MockApplication(), sharedPref)
        testObject.loginModel.userName = ""
        testObject.errorResponse.observeForever(errorObserver)

        testObject.executeLogin()
        verify(errorObserver).onChanged(R.string.login_failure_username)
    }


}