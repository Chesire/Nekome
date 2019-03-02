package com.chesire.malime.flow.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chesire.malime.core.api.AuthApi
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val auth: AuthApi) : ViewModel() {
    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    fun login() {

    }
}
