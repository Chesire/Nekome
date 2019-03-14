package com.chesire.malime.flow.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chesire.malime.IOContext
import com.chesire.malime.core.Resource
import com.chesire.malime.core.api.AuthApi
import com.chesire.malime.repo.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class LoginViewModel @Inject constructor(
    private val auth: AuthApi,
    private val user: UserRepository,
    @IOContext private val ioContext: CoroutineContext
) : ViewModel() {
    private val job = Job()
    private val ioScope = CoroutineScope(job + ioContext)
    private val _loginStatus = MutableLiveData<LoginStatus>()
    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val loginStatus: LiveData<LoginStatus>
        get() = _loginStatus

    fun login() = ioScope.launch {
        if (!validParams()) {
            return@launch
        }

        executeLogin(username.value!!, password.value!!)
    }

    private fun validParams(): Boolean {
        return when {
            username.value.isNullOrEmpty() -> {
                _loginStatus.postValue(LoginStatus.EmptyUsername)
                false
            }
            password.value.isNullOrEmpty() -> {
                _loginStatus.postValue(LoginStatus.EmptyPassword)
                false
            }
            else -> true
        }
    }

    private suspend fun executeLogin(name: String, pw: String) {
        val result = auth.login(name, pw)
        when (result) {
            is Resource.Success -> executeGetUser()
            is Resource.Error -> {
                Timber.e("Error logging in - [${result.code}] ${result.msg}")
                _loginStatus.postValue(LoginStatus.Error)
            }
        }
    }

    private suspend fun executeGetUser() {
        val result = user.retrieveRemoteUser()
        when (result) {
            is Resource.Success -> {
                user.insertUser(result.data)
                _loginStatus.postValue(LoginStatus.Success)
            }
            is Resource.Error -> {
                Timber.e("Error getting user - [${result.code}] ${result.msg}")
                auth.clearAuth()
                _loginStatus.postValue(LoginStatus.Error)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    enum class LoginStatus {
        EmptyUsername,
        EmptyPassword,
        Error,
        Success
    }
}
