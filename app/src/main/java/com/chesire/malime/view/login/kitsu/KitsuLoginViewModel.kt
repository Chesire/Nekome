package com.chesire.malime.view.login.kitsu

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableBoolean
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import com.chesire.malime.R
import com.chesire.malime.kitsu.KitsuManagerFactory
import com.chesire.malime.kitsu.models.LoginResponse
import com.chesire.malime.util.SharedPref
import com.chesire.malime.util.SupportedService
import com.chesire.malime.view.login.LoginModel
import com.chesire.malime.view.login.LoginStatus
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction

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
        if (!isValid(loginModel.email, loginModel.userName, loginModel.password)) {
            return
        }

        val kitsuManager = kitsuManagerFactory.get()
        disposables.add(
            kitsuManager
                .login(loginModel.email, loginModel.password)
                .zipWith(kitsuManager.getUserId(loginModel.userName),
                    BiFunction { response: LoginResponse, userId: Int ->
                        sharedPref.putPrimaryService(SupportedService.Kitsu)
                            .putUserId(userId)
                            .putAuth(response.accessToken)
                    }
                )
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
                }
                .subscribe()
        )
    }

    private fun isValid(email: String, username: String, password: String): Boolean {
        return when {
            email.isBlank() -> {
                errorResponse.value = R.string.login_failure_email
                false
            }
            username.isBlank() -> {
                errorResponse.value = R.string.login_failure_display_name
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