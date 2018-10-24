package com.chesire.malime.view.preferences

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.chesire.malime.R

class PrefActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_pref)
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_pref_frame, PrefFragment.newInstance(), PrefFragment.tag)
            .commit()
    }
}
