package com.chesire.malime.view.login.mal

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableBoolean
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import com.chesire.malime.R
import com.chesire.malime.core.flags.SupportedService
import com.chesire.malime.mal.api.MalManagerFactory
import com.chesire.malime.util.SharedPref
import com.chesire.malime.view.login.LoginModel
import com.chesire.malime.view.login.LoginStatus
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable

private const val malSignupUrl = "https://myanimelist.net/register.php"

class MalLoginViewModel(
    private val context: Application,
    private val sharedPref: SharedPref,
    private val malManagerFactory: MalManagerFactory,
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
            .launchUrl(context, Uri.parse(malSignupUrl))
    }

    fun executeLogin(credentials: String) {
        if (!isValid(loginModel.userName, loginModel.password, credentials)) {
            return
        }

        val malManager = malManagerFactory.get(credentials, loginModel.userName)
        disposables.add(malManager.login(loginModel.userName, loginModel.password)
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
            .subscribe(
                {
                    sharedPref.putPrimaryService(SupportedService.MyAnimeList)
                        .putUsername(loginModel.userName)
                        .putAuth(credentials)
                    loginResponse.value = LoginStatus.SUCCESS
                },
                {
                    errorResponse.value = R.string.login_failure
                    loginResponse.value = LoginStatus.ERROR
                }
            )
        )
    }

    private fun isValid(username: String, password: String, credentials: String): Boolean {
        return when {
            username.isBlank() -> {
                errorResponse.value = R.string.login_failure_username
                false
            }
            password.isBlank() -> {
                errorResponse.value = R.string.login_failure_password
                false
            }
            credentials.isBlank() -> {
                errorResponse.value = R.string.login_failure
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