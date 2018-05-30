package com.chesire.malime.view.login

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.chesire.malime.mal.MalManagerFactory
import com.chesire.malime.util.SharedPref
import com.chesire.malime.view.login.mal.MalLoginViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginViewModelFactory(
    private val application: Application,
    private val sharedPref: SharedPref,
    private val malManagerFactory: MalManagerFactory
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MalLoginViewModel(
            application,
            sharedPref,
            malManagerFactory,
            Schedulers.io(),
            AndroidSchedulers.mainThread()
        ) as T
    }
}