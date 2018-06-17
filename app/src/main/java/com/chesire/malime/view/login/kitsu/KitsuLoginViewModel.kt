package com.chesire.malime.view.login.kitsu

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableBoolean
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import com.chesire.malime.R
import com.chesire.malime.core.flags.SupportedService
import com.chesire.malime.core.models.LoginResponse
import com.chesire.malime.kitsu.api.KitsuManagerFactory
import com.chesire.malime.util.SharedPref
import com.chesire.malime.view.login.LoginModel
import com.chesire.malime.view.login.LoginStatus
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable

private const val kitsuSignupUrl = "https://kitsu.io/explore/anime"

class KitsuLoginViewModel(
    private val context: Application,
    private val sharedPref: SharedPref,
    private val kitsuManagerFactory: KitsuManagerFactory,
    private val subscribeScheduler: Scheduler,
    private val observeScheduler: Scheduler
) : AndroidViewModel(context) {
    private val disposables = CompositeDisposable()
    val loginResponse = MutableLiveData<LoginStatus>()
    val errorResponse = MutableLiveData<Int>()
    val attemptingLogin = ObservableBoolean()
    val loginModel = LoginModel()

    fun createAccount() {
        CustomTabsIntent.Builder()
            .build()
            .launchUrl(context, Uri.parse(kitsuSignupUrl))
    }

    fun executeLogin() {
        if (!isValid(loginModel.userName, loginModel.password)) {
            return
        }

        var apiResponse: LoginResponse? = null
        val kitsuManager = kitsuManagerFactory.get()
        disposables.add(kitsuManager.login(loginModel.userName, loginModel.password)
            .flatMap {
                apiResponse = it
                val authenticatedManager = kitsuManagerFactory.get(it.authToken)
                return@flatMap authenticatedManager.getUserId()
            }
            .subscribeOn(subscribeScheduler)
            .observeOn(observeScheduler)
            .doOnSubscribe {
                attemptingLogin.set(true)
                loginResponse.value = LoginStatus.PROCESSING
            }
            .doFinally {
                attemptingLogin.set(false)
                loginResponse.value = LoginStatus.FINISHED
            }
            .doOnError {
                errorResponse.value = R.string.login_failure
                loginResponse.value = LoginStatus.ERROR
            }
            .doOnSuccess {
                loginResponse.value = LoginStatus.SUCCESS

                sharedPref.putPrimaryService(SupportedService.Kitsu)
                    .putUserId(it)
                    .putAuth(apiResponse!!.authToken)
                    .putRefresh(apiResponse!!.refreshToken)
            }
            .subscribe()
        )
    }

    private fun isValid(username: String, password: String): Boolean {
        return when {
            username.isBlank() -> {
                errorResponse.value = R.string.login_failure_email
                false
            }
            password.isBlank() -> {
                errorResponse.value = R.string.login_failure_password
                false
            }
            else -> true
        }
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}