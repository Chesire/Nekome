package com.chesire.malime.flow

import android.content.Intent
import android.os.Bundle
import com.chesire.malime.kitsu.AuthProvider
import dagger.android.DaggerActivity
import javax.inject.Inject

class LaunchActivity : DaggerActivity() {
    @Inject
    lateinit var authProvider: AuthProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // if not performed oob, go to oob flow

        val clazz = when {
            authProvider.accessToken.isEmpty() -> SetupActivity::class.java
            else -> OverviewActivity::class.java
        }
        startActivity(Intent(this, clazz))
        finish()
    }
}
