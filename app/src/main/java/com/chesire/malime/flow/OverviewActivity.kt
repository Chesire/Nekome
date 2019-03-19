package com.chesire.malime.flow

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.transaction
import com.chesire.malime.R
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.flow.activity.ActivityFragment
import com.chesire.malime.flow.profile.ProfileFragment
import com.chesire.malime.flow.series.detail.SeriesDetailFragment
import com.chesire.malime.flow.series.list.SeriesListener
import com.chesire.malime.flow.series.list.anime.AnimeFragment
import com.chesire.malime.flow.series.list.manga.MangaFragment
import com.chesire.malime.flow.series.search.SearchFragment
import com.chesire.malime.flow.settings.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_overview.activityOverviewNavigation

class OverviewActivity :
    AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener,
    SeriesListener {

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

    override fun loadDetailFragment(seriesModel: SeriesModel) {
        supportFragmentManager.transaction {
            replace(
                R.id.activityOverviewContent,
                SeriesDetailFragment.newInstance(seriesModel),
                SeriesDetailFragment.TAG
            ).addToBackStack(SeriesDetailFragment.TAG)
        }
    }

    override fun loadSearchFragment() {
        supportFragmentManager.transaction {
            replace(
                R.id.activityOverviewContent,
                SearchFragment.newInstance(),
                SearchFragment.TAG
            ).addToBackStack(SearchFragment.TAG)
        }
    }
}
