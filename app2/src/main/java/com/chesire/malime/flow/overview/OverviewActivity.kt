package com.chesire.malime.flow.overview

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.chesire.malime.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_overview.activityOverviewNavigation

class OverviewActivity :
    AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)

        activityOverviewNavigation.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId) {
            R.id.overviewNavAnime -> {
                return true
            }
            R.id.overviewNavManga -> {
                return true
            }
            R.id.overviewNavProfile -> {
                return true
            }
            R.id.overviewNavActivity -> {
                return true
            }
            R.id.overviewNavSettings -> {
                return true
            }
            else -> return false
        }
    }
}
