package com.chesire.malime

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class LaunchActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = SharedPref(this)
        val loadIntent =
            if (sharedPref.getAuth().isNotEmpty() && sharedPref.getUsername().isNotEmpty()) {
                Intent(this, MainActivity::class.java)
            } else {
                Intent(this, LoginActivity::class.java)
            }

        startActivity(loadIntent)
        finish()
    }
}