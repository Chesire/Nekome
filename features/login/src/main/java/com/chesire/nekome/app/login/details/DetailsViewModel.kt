package com.chesire.nekome.app.login.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekome.account.UserRepository
import com.chesire.nekome.auth.api.AuthApi
import com.chesire.nekome.core.Resource
import com.hadilq.liveevent.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * ViewModel to aid with logging a user in via the [DetailsFragment].
 */
@HiltViewModel
class DetailsViewModel @Inject constructor(
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
                if (result.code == Resource.Error.InvalidCredentials) {
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
