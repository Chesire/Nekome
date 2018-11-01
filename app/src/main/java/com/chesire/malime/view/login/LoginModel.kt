package com.chesire.malime.view.login

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class LoginModel : BaseObservable() {
    @Bindable
    var userName = ""
    @Bindable
    var password = ""
}
