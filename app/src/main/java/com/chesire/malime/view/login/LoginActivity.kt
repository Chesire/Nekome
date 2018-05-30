package com.chesire.malime.view.login

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.chesire.malime.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        supportActionBar?.hide()
        actionBar?.hide()

        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_login_layout, LoginFragment.newInstance(), LoginFragment.tag)
            .commit()
    }
}
