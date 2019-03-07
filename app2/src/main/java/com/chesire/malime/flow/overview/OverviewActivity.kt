package com.chesire.malime.flow.overview

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.transaction
import com.chesire.malime.R
import com.chesire.malime.flow.overview.activity.ActivityFragment
import com.chesire.malime.flow.overview.anime.AnimeFragment
import com.chesire.malime.flow.overview.manga.MangaFragment
import com.chesire.malime.flow.overview.profile.ProfileFragment
import com.chesire.malime.flow.overview.settings.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_overview.activityOverviewNavigation

class OverviewActivity :
    AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)

        activityOverviewNavigation.setOnNavigationItemSelectedListener(this)

        if (savedInstanceState == null) {
            loadFragment(AnimeFragment::class.java)
        }
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId) {
            R.id.overviewNavAnime -> loadFragment(AnimeFragment::class.java)
            R.id.overviewNavManga -> loadFragment(MangaFragment::class.java)
            R.id.overviewNavProfile -> loadFragment(ProfileFragment::class.java)
            R.id.overviewNavActivity -> loadFragment(ActivityFragment::class.java)
            R.id.overviewNavSettings -> loadFragment(SettingsFragment::class.java)
            else -> return false
        }
        return true
    }

    private fun loadFragment(clazz: Class<out Fragment>) {
        val (fragment, tag) = when (clazz) {
            AnimeFragment::class.java -> AnimeFragment.newInstance() to AnimeFragment.TAG
            MangaFragment::class.java -> MangaFragment.newInstance() to MangaFragment.TAG
            ProfileFragment::class.java -> ProfileFragment.newInstance() to ProfileFragment.TAG
            ActivityFragment::class.java -> ActivityFragment.newInstance() to ActivityFragment.TAG
            SettingsFragment::class.java -> SettingsFragment.newInstance() to SettingsFragment.TAG
            else -> error("Unexpected clazz provided $clazz")
        }
        supportFragmentManager.transaction {
            replace(R.id.activityOverviewContent, fragment, tag)
        }
    }
}
