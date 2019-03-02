package com.chesire.malime.flow.login

import androidx.lifecycle.ViewModel
import com.chesire.malime.core.api.AuthApi
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val auth: AuthApi) : ViewModel() {

}
