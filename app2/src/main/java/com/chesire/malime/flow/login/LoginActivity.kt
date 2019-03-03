package com.chesire.malime.flow.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chesire.malime.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportFragmentManager.beginTransaction()
            .replace(R.id.activityLoginContainer, LoginFragment.newInstance())
            .commit()
    }
}
