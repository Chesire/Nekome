package com.chesire.malime

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.chesire.malime.util.SharedPref
import com.chesire.malime.view.MainActivity
import com.chesire.malime.view.login.LoginActivity
import dagger.android.AndroidInjection
import javax.inject.Inject

class LaunchActivity : Activity() {
    @Inject
    lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        val loadIntent =
            if (sharedPref.getAuth().authToken.isNotBlank() &&
                (sharedPref.getUsername().isNotEmpty() || sharedPref.getUserId() != 0)
            ) {
                Intent(this, MainActivity::class.java)
            } else {
                Intent(this, LoginActivity::class.java)
            }

        startActivity(loadIntent)
        finish()
    }
}