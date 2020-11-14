package com.chesire.nekome.app.login.details

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekome.account.UserRepository
import com.chesire.nekome.server.Resource
import com.chesire.nekome.server.api.AuthApi
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * ViewModel to aid with logging a user in via the [DetailsFragment].
 */
class DetailsViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val auth: AuthApi,
    private val user: UserRepository
) : ViewModel() {

    private val _loginStatus = LiveEvent<LoginStatus>()
    val loginStatus: LiveData<LoginStatus> = _loginStatus

    /**
     * Send a login request to Kitsu, with the credentials of [username] and [password].
     *
     * The result of this operation will be posted to [loginStatus].
     */
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
