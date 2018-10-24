package com.chesire.malime.view.login.mal

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import com.chesire.malime.R
import com.chesire.malime.core.flags.SupportedService
import com.chesire.malime.mal.api.MalAuthorizer
import com.chesire.malime.mal.api.MalManager
import com.chesire.malime.util.IOScheduler
import com.chesire.malime.util.SharedPref
import com.chesire.malime.util.UIScheduler
import com.chesire.malime.view.login.LoginModel
import com.chesire.malime.view.login.LoginStatus
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

// When MAL support is added again, the AppModule needs to be updated to allow usage of the Mal service

class MalLoginViewModel @Inject constructor(
    private val sharedPref: SharedPref,
    private val malManager: MalManager,
    private val authorizer: MalAuthorizer
) : ViewModel() {
    private val disposables = CompositeDisposable()
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
                    loginResponse.value = LoginStatus.SUCCESS

                    sharedPref.primaryService = SupportedService.MyAnimeList
                    authorizer.storeAuthDetails(it)
                    authorizer.storeUser(loginModel.userName)
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
