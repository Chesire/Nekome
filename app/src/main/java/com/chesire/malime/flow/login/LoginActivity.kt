package com.chesire.malime.flow.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.malime.R

@LogLifecykle
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
}
