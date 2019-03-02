package com.chesire.malime.flow.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chesire.malime.IOContext
import com.chesire.malime.core.Resource
import com.chesire.malime.core.api.AuthApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class LoginViewModel @Inject constructor(
    private val auth: AuthApi,
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
        if (username.value.isNullOrEmpty()) {
            _loginStatus.postValue(LoginStatus.EmptyUsername)
            return@launch
        } else if (password.value.isNullOrEmpty()) {
            _loginStatus.postValue(LoginStatus.EmptyPassword)
            return@launch
        }

        val result = auth.login(username.value!!, password.value!!)
        when (result) {
            is Resource.Success -> _loginStatus.postValue(LoginStatus.Success)
            is Resource.Error -> _loginStatus.postValue(LoginStatus.Error)
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
