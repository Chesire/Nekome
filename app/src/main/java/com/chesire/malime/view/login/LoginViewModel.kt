package com.chesire.malime.view.login

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.net.Uri
import android.support.customtabs.CustomTabsIntent

private const val malSignupUrl = "https://myanimelist.net/register.php"

class LoginViewModel(private val context: Application) : AndroidViewModel(context) {
    val loginModel = LoginModel()

    fun createMalAccount() {
        CustomTabsIntent.Builder()
            .build()
            .launchUrl(context, Uri.parse(malSignupUrl))
    }
}