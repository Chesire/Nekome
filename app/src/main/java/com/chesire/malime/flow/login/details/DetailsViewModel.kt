package com.chesire.malime.flow.login.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.malime.account.UserRepository
import com.chesire.malime.server.Resource
import com.chesire.malime.server.api.AuthApi
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class DetailsViewModel @Inject constructor(
    private val auth: AuthApi,
    private val user: UserRepository
) : ViewModel() {
    private val _loginStatus = LiveEvent<LoginStatus>()
    val loginStatus: LiveData<LoginStatus> = _loginStatus

    fun login(username: String, password: String) = viewModelScope.launch {
        when {
            username.isEmpty() -> _loginStatus.postValue(LoginStatus.EmptyUsername)
            password.isEmpty() -> _loginStatus.postValue(LoginStatus.EmptyPassword)
            else -> {
                _loginStatus.postValue(LoginStatus.Loading)
                executeLogin(username, password)
            }
        }
    }

    private suspend fun executeLogin(name: String, pw: String) {
        when (val result = auth.login(name, pw)) {
            is Resource.Success -> executeGetUser()
            is Resource.Error -> {
                Timber.e("Error logging in - [${result.code}] ${result.msg}")
                if (result.code == 401) {
                    _loginStatus.postValue(LoginStatus.InvalidCredentials)
                } else {
                    _loginStatus.postValue(LoginStatus.Error)
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
