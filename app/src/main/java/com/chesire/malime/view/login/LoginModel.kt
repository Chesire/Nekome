package com.chesire.malime.view.login

import android.databinding.BaseObservable
import android.databinding.Bindable

class LoginModel : BaseObservable() {
    @Bindable
    var email = ""
    @Bindable
    var userName = ""
    @Bindable
    var password = ""
}