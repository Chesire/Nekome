package com.chesire.malime.flow

import android.content.Intent
import android.os.Bundle
import com.chesire.malime.flow.overview.OverviewActivity
import dagger.android.DaggerActivity

class LaunchActivity : DaggerActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, OverviewActivity::class.java))
        finish()
        // if not performed oob, go to oob flow

        // if logged in, go to the logged in flow

        // if not logged in go to the login flow
    }
}
