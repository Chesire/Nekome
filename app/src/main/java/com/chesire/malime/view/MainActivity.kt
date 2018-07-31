package com.chesire.malime.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.chesire.malime.R
import com.chesire.malime.core.flags.ItemType
import com.chesire.malime.core.repositories.Authorization
import com.chesire.malime.core.room.MalimeDatabase
import com.chesire.malime.service.PeriodicUpdateHelper
import com.chesire.malime.service.RefreshTokenHelper
import com.chesire.malime.util.SharedPref
import com.chesire.malime.util.UrlLoader
import com.chesire.malime.view.login.LoginActivity
import com.chesire.malime.view.maldisplay.MalDisplayFragment
import com.chesire.malime.view.preferences.PrefActivity
import com.chesire.malime.view.preferences.SortOption
import com.chesire.malime.view.search.SearchFragment
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {
    private var currentDisplayedFragmentTagBundleId = "currentFragment"
    private var currentDisplayedFragmentTag = ""

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var sharedPref: SharedPref
    @Inject
    lateinit var authorization: Authorization
    @Inject
    lateinit var db: MalimeDatabase
    @Inject
    lateinit var urlLoader: UrlLoader

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

        PeriodicUpdateHelper().schedule(this, sharedPref)
        RefreshTokenHelper().schedule(this, sharedPref)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString(currentDisplayedFragmentTagBundleId, currentDisplayedFragmentTag)
        super.onSaveInstanceState(outState)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when {
            item?.itemId == R.id.menu_options_view_profile -> {
                launchProfile()
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
                    .setPositiveButton(android.R.string.yes) { _, _ ->
                        logout()
                    }
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

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    private fun logout() {
        val handlerThread = HandlerThread("ClearRoomDBThread")
        handlerThread.start()
        Handler(handlerThread.looper).post {
            Timber.d("Clearing the internal Room DB")
            db.clearAllTables()

            Timber.d("Clearing internal login details")
            authorization.logoutAll()

            Timber.d("Clearing the service helpers")
            PeriodicUpdateHelper().cancel(this, sharedPref)
            RefreshTokenHelper().cancel(this, sharedPref)

            Timber.d("Navigating to LoginActivity")
            startActivity(Intent(this, LoginActivity::class.java))
            finish()

            Timber.d("Closing the handler thread")
            handlerThread.quitSafely()
        }
    }

    private fun spawnSortDialog() {
        var sortOption = sharedPref.getSortOption().id

        AlertDialog.Builder(this)
            .setTitle(R.string.sort_dialog_title)
            .setSingleChoiceItems(
                SortOption.getOptionsStrings(applicationContext),
                sortOption
            ) { _, which ->
                sortOption = which
            }
            .setPositiveButton(android.R.string.ok) { _, _ ->
                sharedPref.setSortOption(SortOption.getOptionFor(sortOption))
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun launchProfile() {
        val primaryService = sharedPref.getPrimaryService()
        urlLoader.loadProfile(this, primaryService, authorization.getUser(primaryService))
    }

    private fun setFragment(fragment: Fragment, fragmentTag: String) {
        // We have to set the title first, or on rotation it doesn't refresh
        title = when (fragmentTag) {
            MalDisplayFragment.malDisplayAnime -> getString(R.string.main_nav_anime)
            MalDisplayFragment.malDisplayManga -> getString(R.string.main_nav_manga)
            SearchFragment.tag -> getString(R.string.main_nav_search)
            else -> getString(R.string.app_name)
        }

        val currentFragment = supportFragmentManager.primaryNavigationFragment
        if (currentFragment == fragment) {
            return
        }

        val transaction = supportFragmentManager.beginTransaction()
        if (currentFragment != null) {
            transaction.detach(currentFragment)
        }

        var taggedFragment = supportFragmentManager.findFragmentByTag(fragmentTag)
        if (taggedFragment == null) {
            taggedFragment = fragment
            transaction.add(R.id.activity_main_frame, taggedFragment, fragmentTag)
        } else {
            transaction.attach(taggedFragment)
        }
        transaction.setPrimaryNavigationFragment(taggedFragment)
        transaction.setReorderingAllowed(true)

        currentDisplayedFragmentTag = fragmentTag
        transaction.commit()
    }
}