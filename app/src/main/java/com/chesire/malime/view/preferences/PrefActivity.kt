package com.chesire.malime.view.preferences

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chesire.malime.R

class PrefActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = getString(R.string.options_settings)
        setContentView(R.layout.activity_pref)
        supportFragmentManager.beginTransaction()
            .replace(R.id.activityPrefFrame, PrefFragment.newInstance(), PrefFragment.tag)
            .commit()
    }
}
