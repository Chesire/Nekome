package com.chesire.malime.view.login.kitsu

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableBoolean
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import com.chesire.malime.R
import com.chesire.malime.core.api.AuthHandler
import com.chesire.malime.core.flags.SupportedService
import com.chesire.malime.core.models.AuthModel
import com.chesire.malime.kitsu.api.KitsuManagerFactory
import com.chesire.malime.util.SharedPref
import com.chesire.malime.view.login.LoginModel
import com.chesire.malime.view.login.LoginStatus
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

private const val kitsuSignupUrl = "https://kitsu.io/explore/anime"

class KitsuLoginViewModel @Inject constructor(
    private val context: Application,
    private val sharedPref: SharedPref,
    private val kitsuManagerFactory: KitsuManagerFactory,
    private val subscribeScheduler: Scheduler,
    private val observeScheduler: Scheduler
) : AndroidViewModel(context), AuthHandler {
    private val disposables = CompositeDisposable()
    private var tempAuthModel: AuthModel = AuthModel("", "", 0)
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

        val kitsuManager = kitsuManagerFactory.get(this)
        disposables.add(kitsuManager.login(loginModel.userName, loginModel.password)
            .flatMap {
                tempAuthModel = AuthModel(
                    it.authToken,
                    it.refreshToken,
                    it.expireAt
                )
                return@flatMap kitsuManagerFactory.get(this).getUserId()
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
                    .setAuth(tempAuthModel)
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

    override fun getAuth(): AuthModel {
        return tempAuthModel
    }

    override fun setAuth(newModel: AuthModel) {
        // Not needed
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}