package com.chesire.malime.view.login

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.chesire.malime.util.SharedPref

class LoginViewModelFactory(
    private val application: Application,
    private val sharedPref: SharedPref
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(application, sharedPref) as T
    }
}