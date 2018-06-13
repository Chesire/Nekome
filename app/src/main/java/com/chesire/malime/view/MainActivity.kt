package com.chesire.malime.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.support.customtabs.CustomTabsIntent
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.chesire.malime.R
import com.chesire.malime.core.flags.ItemType
import com.chesire.malime.core.room.MalimeDatabase
import com.chesire.malime.util.SharedPref
import com.chesire.malime.util.updateservice.PeriodicUpdateHelper
import com.chesire.malime.view.login.LoginActivity
import com.chesire.malime.view.maldisplay.MalDisplayFragment
import com.chesire.malime.view.preferences.PrefActivity
import com.chesire.malime.view.preferences.SortOption
import com.chesire.malime.view.search.Search2Fragment
import timber.log.Timber

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
                    tag = MalDisplayFragment.malDisplayAnime
                    fragment = supportFragmentManager.findFragmentByTag(tag)
                            ?: MalDisplayFragment.newInstance(ItemType.Anime)
                }
                R.id.menu_main_navigation_manga -> {
                    tag = MalDisplayFragment.malDisplayManga
                    fragment = supportFragmentManager.findFragmentByTag(tag)
                            ?: MalDisplayFragment.newInstance(ItemType.Manga)
                }
                else -> {
                    tag = Search2Fragment.tag
                    fragment = supportFragmentManager.findFragmentByTag(tag)
                            ?: Search2Fragment.newInstance()
                }
            }

            setFragment(fragment, tag)
            true
        }

        if (savedInstanceState == null) {
            setFragment(
                MalDisplayFragment.newInstance(ItemType.Anime),
                MalDisplayFragment.malDisplayAnime
            )
        } else {
            currentDisplayedFragmentTag =
                    savedInstanceState.getString(currentDisplayedFragmentTagBundleId)
            setFragment(
                supportFragmentManager.findFragmentByTag(currentDisplayedFragmentTag),
                currentDisplayedFragmentTag
            )
        }

        //PeriodicUpdateHelper().schedule(this)
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
                        logout()
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

    private fun logout() {
        val handlerThread = HandlerThread("ClearRoomDBThread")
        handlerThread.start()
        Handler(handlerThread.looper).post({
            Timber.d("Clearing the internal Room DB")
            MalimeDatabase.getInstance(this).clearAllTables()

            Timber.d("Clearing internal login details")
            sharedPref.clearLoginDetails()

            Timber.d("Clearing the update helper")
            PeriodicUpdateHelper().cancel(this)

            Timber.d("Navigating to LoginActivity")
            startActivity(Intent(this, LoginActivity::class.java))
            finish()

            Timber.d("Closing the handler thread")
            handlerThread.quitSafely()
        })
    }

    private fun spawnSortDialog() {
        var sortOption = sharedPref.getSortOption().id

        AlertDialog.Builder(this)
            .setTitle(R.string.sort_dialog_title)
            .setSingleChoiceItems(
                SortOption.getOptionsStrings(applicationContext), sortOption, { _, which ->
                    sortOption = which
                })
            .setPositiveButton(android.R.string.ok, { _, _ ->
                sharedPref.setSortOption(SortOption.getOptionFor(sortOption))
            })
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun setFragment(fragment: Fragment, fragmentTag: String) {
        title = when (fragmentTag) {
            MalDisplayFragment.malDisplayAnime -> getString(R.string.main_nav_anime)
            MalDisplayFragment.malDisplayManga -> getString(R.string.main_nav_manga)
            Search2Fragment.tag -> getString(R.string.main_nav_search)
            else -> getString(R.string.app_name)
        }

        currentDisplayedFragmentTag = fragmentTag
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_main_frame, fragment, fragmentTag)
            .commit()
    }
}