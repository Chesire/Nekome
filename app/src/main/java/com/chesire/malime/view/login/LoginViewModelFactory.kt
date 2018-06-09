package com.chesire.malime.view.login

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.chesire.malime.kitsu.api.KitsuManagerFactory
import com.chesire.malime.mal.api.MalManagerFactory
import com.chesire.malime.util.SharedPref
import com.chesire.malime.view.login.kitsu.KitsuLoginViewModel
import com.chesire.malime.view.login.mal.MalLoginViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.InvalidClassException

class LoginViewModelFactory(
    private val application: Application,
    private val sharedPref: SharedPref
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(MalLoginViewModel::class.java) -> return MalLoginViewModel(
                application,
                sharedPref,
                MalManagerFactory(),
                Schedulers.io(),
                AndroidSchedulers.mainThread()
            ) as T
            modelClass.isAssignableFrom(KitsuLoginViewModel::class.java) -> return KitsuLoginViewModel(
                application,
                sharedPref,
                KitsuManagerFactory(),
                Schedulers.io(),
                AndroidSchedulers.mainThread()
            ) as T
            else -> throw InvalidClassException("")
        }
    }
}