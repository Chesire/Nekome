package com.chesire.malime.flow.login.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.malime.core.Resource
import com.chesire.malime.core.api.AuthApi
import com.chesire.malime.repo.UserRepository
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class DetailsViewModel @Inject constructor(
    private val auth: AuthApi,
    private val user: UserRepository
) : ViewModel() {
    private val _loginStatus = LiveEvent<LoginStatus>()
    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val loginStatus: LiveData<LoginStatus> = _loginStatus

    fun login() = viewModelScope.launch {
        if (!validParams()) {
            return@launch
        }

        _loginStatus.postValue(LoginStatus.Loading)
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
        when (val result = auth.login(name, pw)) {
            is Resource.Success -> executeGetUser()
            is Resource.Error -> {
                Timber.e("Error logging in - [${result.code}] ${result.msg}")
                when (result.code) {
                    401 -> _loginStatus.postValue(LoginStatus.InvalidCredentials)
                    else -> _loginStatus.postValue(LoginStatus.Error)
                }
            }
        }
    }

    private suspend fun executeGetUser() {
        when (val result = user.refreshUser()) {
            is Resource.Success -> _loginStatus.postValue(LoginStatus.Success)
            is Resource.Error -> {
                Timber.e("Error getting user - [${result.code}] ${result.msg}")
                auth.clearAuth()
                _loginStatus.postValue(LoginStatus.Error)
            }
        }
    }
}
