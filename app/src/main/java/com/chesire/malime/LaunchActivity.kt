package com.chesire.malime

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.chesire.malime.util.SharedPref
import com.chesire.malime.view.MainActivity
import com.chesire.malime.view.login.LoginActivity

class LaunchActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = SharedPref(this)
        val loadIntent =
            if (sharedPref.getAuth().isNotEmpty()
                && (sharedPref.getUsername().isNotEmpty() || sharedPref.getUserId() != 0)
            ) {
                Intent(this, MainActivity::class.java)
            } else {
                Intent(this, LoginActivity::class.java)
            }

        startActivity(loadIntent)
        finish()
    }
}