package com.chesire.nekome.app.login.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.nekome.core.Resource
import com.chesire.nekome.datasource.auth.AccessTokenRepository
import com.chesire.nekome.datasource.auth.AccessTokenResult
import com.chesire.nekome.datasource.user.UserRepository
import com.hadilq.liveevent.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * ViewModel to aid with logging a user in via the [DetailsFragment].
 */
@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val auth: AccessTokenRepository,
    private val user: UserRepository
) : ViewModel() {

    private val _loginStatus = LiveEvent<LoginStatus>()
    val loginStatus: LiveData<LoginStatus>
        get() = _loginStatus

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
        val result = auth.login(name, pw)
        Timber.d("executeLogin result - [$result]")
        when (result) {
            AccessTokenResult.Success -> executeGetUser()
            AccessTokenResult.InvalidCredentials ->
                _loginStatus.postValue(LoginStatus.InvalidCredentials)
            AccessTokenResult.CommunicationError ->
                _loginStatus.postValue(LoginStatus.Error)
        }
    }

    private suspend fun executeGetUser() {
        when (val result = user.refreshUser()) {
            is Resource.Success -> _loginStatus.postValue(LoginStatus.Success)
            is Resource.Error -> {
                Timber.e("Error getting user - [${result.code}] ${result.msg}")
                auth.clear()
                _loginStatus.postValue(LoginStatus.Error)
            }
        }
    }
}
