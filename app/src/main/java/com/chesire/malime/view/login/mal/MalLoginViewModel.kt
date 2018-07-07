package com.chesire.malime.view.login.mal

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import com.chesire.malime.R
import com.chesire.malime.core.api.AuthHandler
import com.chesire.malime.core.flags.SupportedService
import com.chesire.malime.core.models.AuthModel
import com.chesire.malime.mal.api.MalManagerFactory
import com.chesire.malime.util.IOScheduler
import com.chesire.malime.util.SharedPref
import com.chesire.malime.util.UIScheduler
import com.chesire.malime.view.login.LoginModel
import com.chesire.malime.view.login.LoginStatus
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MalLoginViewModel @Inject constructor(
    private val sharedPref: SharedPref,
    private val malManagerFactory: MalManagerFactory
) : ViewModel(), AuthHandler {
    private val disposables = CompositeDisposable()
    private lateinit var tempAuthModel: AuthModel
    val loginResponse = MutableLiveData<LoginStatus>()
    val errorResponse = MutableLiveData<Int>()
    val attemptingLogin = ObservableBoolean()
    val loginModel = LoginModel()

    @Inject
    @field:IOScheduler
    lateinit var subscribeScheduler: Scheduler

    @Inject
    @field:UIScheduler
    lateinit var observeScheduler: Scheduler

    fun executeLogin(credentials: String) {
        if (!isValid(loginModel.userName, loginModel.password, credentials)) {
            return
        }

        tempAuthModel = AuthModel(credentials, "", 0)
        val malManager = malManagerFactory.get(this, loginModel.userName)
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
                        .setAuth(AuthModel(credentials, "", 0))
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