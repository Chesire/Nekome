package com.chesire.malime

import android.os.Bundle
import dagger.android.DaggerActivity

class LaunchActivity : DaggerActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // if not performed oob, go to oob flow

        // if logged in, go to the logged in flow

        // if not logged in go to the login flow
    }
}
