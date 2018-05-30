package com.chesire.malime.view.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.chesire.malime.R
import com.chesire.malime.view.MainActivity
import com.chesire.malime.view.login.login.LoginFragment
import com.chesire.malime.view.login.service.ServiceSelectionFragment

class LoginActivity : AppCompatActivity(), LoginInteractor {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        supportActionBar?.hide()
        actionBar?.hide()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.activity_login_layout,
                    ServiceSelectionFragment.newInstance(),
                    ServiceSelectionFragment.tag
                )
                .commit()
        }
    }

    override fun serviceSelected() {
        // Depending on the service selected, might go to a different fragment?
        // Or pass in a param for the fields needed
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_login_layout, LoginFragment.newInstance(), LoginFragment.tag)
            .addToBackStack(null)
            .commit()
    }

    override fun loginSuccessful() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
