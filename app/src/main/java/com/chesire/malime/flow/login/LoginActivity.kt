package com.chesire.malime.flow.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.transaction
import com.chesire.malime.R
import com.chesire.malime.flow.OverviewActivity

class LoginActivity : AppCompatActivity(), LoginFragment.LoginListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (savedInstanceState == null) {
            supportFragmentManager.transaction {
                replace(R.id.activityLoginContainer, LoginFragment.newInstance())
            }
        }
    }

    override fun onLoginSuccess() {
        startActivity(Intent(this, OverviewActivity::class.java))
        finish()
    }
}
