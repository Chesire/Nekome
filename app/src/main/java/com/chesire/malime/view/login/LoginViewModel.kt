package com.chesire.malime.view.login

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import com.chesire.malime.R
import com.chesire.malime.kitsu.KitsuManager
import com.chesire.malime.mal.MalManagerFactory
import com.chesire.malime.util.SharedPref
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable

private const val malSignupUrl = "https://myanimelist.net/register.php"

class LoginViewModel(
    private val context: Application,
    private val sharedPref: SharedPref,
    private val malManagerFactory: MalManagerFactory,
    private val subscribeScheduler: Scheduler,
    private val observeScheduler: Scheduler
) : AndroidViewModel(context) {
    private val disposables = CompositeDisposable()
    val loginResponse = MutableLiveData<LoginStatus>()
    val errorResponse = MutableLiveData<Int>()
    val loginModel = LoginModel()

    fun createMalAccount() {
        CustomTabsIntent.Builder()
            .build()
            .launchUrl(context, Uri.parse(malSignupUrl))
    }

    fun executeLogin(credentials: String) {
        if (!isValid(loginModel.userName, loginModel.password, credentials)) {
            return
        }

        //val malManager = malManagerFactory.get(credentials, loginModel.userName)
        val kitsuManager = KitsuManager()
        disposables.add(kitsuManager.login(loginModel.userName, loginModel.password)
            .subscribeOn(subscribeScheduler)
            .observeOn(observeScheduler)
            .doOnSubscribe { _ ->
                loginResponse.value = LoginStatus.PROCESSING
            }
            .doFinally {
                loginResponse.value = LoginStatus.FINISHED
            }
            .subscribe(
                { _ ->
                    sharedPref
                        .putUsername(loginModel.userName)
                        .putAuth(credentials)
                    loginResponse.value = LoginStatus.SUCCESS
                },
                { _ ->
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