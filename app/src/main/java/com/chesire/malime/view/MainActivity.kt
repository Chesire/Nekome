package com.chesire.malime.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.chesire.malime.R
import com.chesire.malime.util.PeriodicUpdateHelper
import com.chesire.malime.util.SharedPref
import com.chesire.malime.view.anime.AnimeFragment
import com.chesire.malime.view.login.LoginActivity
import com.chesire.malime.view.manga.MangaFragment
import com.chesire.malime.view.preferences.PrefActivity
import com.chesire.malime.view.search.SearchFragment

class MainActivity : AppCompatActivity() {
    private val sharedPref: SharedPref by lazy {
        SharedPref(this)
    }
    private var currentDisplayedFragmentTagBundleId = "currentFragment"
    private var currentDisplayedFragmentTag = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.activity_main_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item: MenuItem ->
            val fragment: Fragment
            val tag: String

            when (item.itemId) {
                R.id.menu_main_navigation_anime -> {
                    tag = AnimeFragment.tag
                    fragment = supportFragmentManager.findFragmentByTag(tag)
                            ?: AnimeFragment.newInstance()
                }
                R.id.menu_main_navigation_manga -> {
                    tag = MangaFragment.tag
                    fragment = supportFragmentManager.findFragmentByTag(tag)
                            ?: MangaFragment.newInstance()
                }
                else -> {
                    tag = SearchFragment.tag
                    fragment = supportFragmentManager.findFragmentByTag(tag)
                            ?: SearchFragment.newInstance()
                }
            }

            setFragment(fragment, tag)
            true
        }

        if (savedInstanceState == null) {
            setFragment(
                AnimeFragment.newInstance(),
                AnimeFragment.tag
            )
        } else {
            currentDisplayedFragmentTag =
                    savedInstanceState.getString(currentDisplayedFragmentTagBundleId)
            setFragment(
                supportFragmentManager.findFragmentByTag(currentDisplayedFragmentTag),
                currentDisplayedFragmentTag
            )
        }

        PeriodicUpdateHelper().schedule(this)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString(currentDisplayedFragmentTagBundleId, currentDisplayedFragmentTag)
        super.onSaveInstanceState(outState)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when {
            item?.itemId == R.id.menu_options_view_profile -> {
                CustomTabsIntent.Builder()
                    .build()
                    .launchUrl(
                        this,
                        Uri.parse("https://myanimelist.net/profile/${sharedPref.getUsername()}")
                    )
                return true
            }
            item?.itemId == R.id.menu_options_settings -> {
                startActivity(Intent(this, PrefActivity::class.java))
                return true
            }
            item?.itemId == R.id.menu_options_log_out -> {
                AlertDialog.Builder(this)
                    .setTitle(R.string.options_log_out)
                    .setMessage(R.string.log_out_confirmation)
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, { _, _ ->
                        sharedPref.clearLoginDetails()
                        PeriodicUpdateHelper().cancel(this)
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    })
                    .show()

                return true
            }
            item?.itemId == R.id.menu_options_sort -> {
                spawnSortDialog()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun spawnSortDialog() {
        var sortOption = sharedPref.getAnimeSortOption()

        AlertDialog.Builder(this)
            .setTitle(R.string.sort_dialog_title)
            .setSingleChoiceItems(R.array.anime_sort_options, sortOption, { _, which ->
                sortOption = which
            })
            .setPositiveButton(android.R.string.ok, { _, _ ->
                sharedPref.setAnimeSortOption(sortOption)
            })
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun setFragment(fragment: Fragment, fragmentTag: String) {
        title = when (fragmentTag) {
            AnimeFragment.tag -> getString(R.string.main_nav_anime)
            MangaFragment.tag -> getString(R.string.main_nav_manga)
            SearchFragment.tag -> getString(R.string.main_nav_search)
            else -> getString(R.string.app_name)
        }

        currentDisplayedFragmentTag = fragmentTag
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_main_frame, fragment, fragmentTag)
            .commit()
    }
}
