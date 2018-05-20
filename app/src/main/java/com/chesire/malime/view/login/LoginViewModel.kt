package com.chesire.malime.view.login

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.util.Base64
import com.chesire.malime.R
import com.chesire.malime.mal.MalManager
import com.chesire.malime.util.SharedPref
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

private const val malSignupUrl = "https://myanimelist.net/register.php"

class LoginViewModel(
    private val context: Application,
    private val sharedPref: SharedPref
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

    fun executeLogin() {
        if (!isValid(loginModel.userName, loginModel.password)) {
            return
        }

        val b64 = Base64.encodeToString(
            "${loginModel.userName}:${loginModel.password}".toByteArray(Charsets.UTF_8),
            Base64.NO_WRAP
        )

        val malManager = MalManager(b64)
        disposables.add(malManager.loginToAccount()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe({ _ ->
                loginResponse.value = LoginStatus.PROCESSING
            })
            .doFinally {
                loginResponse.value = LoginStatus.FINISHED
            }
            .subscribe(
                { _ ->
                    sharedPref
                        .putUsername(loginModel.userName)
                        .putAuth(b64)
                    loginResponse.value = LoginStatus.SUCCESS
                },
                { _ ->
                    errorResponse.value = R.string.login_failure
                    loginResponse.value = LoginStatus.ERROR
                }
            )
        )
    }

    private fun isValid(username: String, password: String): Boolean {
        return when {
            username.isBlank() -> {
                errorResponse.value = R.string.login_failure_username
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