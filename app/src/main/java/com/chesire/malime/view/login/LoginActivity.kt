package com.chesire.malime.view.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.chesire.malime.R
import com.chesire.malime.core.flags.SupportedService
import com.chesire.malime.view.MainActivity
import com.chesire.malime.view.login.kitsu.KitsuLoginFragment
import com.chesire.malime.view.login.mal.MalLoginFragment
import dagger.android.support.DaggerAppCompatActivity

class LoginActivity : DaggerAppCompatActivity(), LoginInteractor {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        supportActionBar?.hide()
        actionBar?.hide()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.activityLoginLayout,
                    KitsuLoginFragment.newInstance(),
                    KitsuLoginFragment.tag
                )
                .commit()
        }
    }

    override fun serviceSelected(service: SupportedService) {
        val fragment: Fragment
        val tag: String

        when (service) {
            SupportedService.MyAnimeList -> {
                fragment = MalLoginFragment.newInstance()
                tag = MalLoginFragment.tag
            }
            SupportedService.Kitsu -> {
                fragment = KitsuLoginFragment.newInstance()
                tag = KitsuLoginFragment.tag
            }
            SupportedService.Unknown -> throw NoWhenBranchMatchedException("Should not reach unknown")
        }

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.activityLoginLayout,
                fragment,
                tag
            )
            .addToBackStack(null)
            .commit()
    }

    override fun loginSuccessful() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
