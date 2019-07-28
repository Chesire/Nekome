package com.chesire.malime.flow

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import androidx.annotation.IdRes
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.malime.AuthCaster
import com.chesire.malime.LogoutHandler
import com.chesire.malime.OverviewNavGraphDirections
import com.chesire.malime.R
import com.chesire.malime.SharedPref
import com.chesire.malime.kitsu.AuthProvider
import com.google.android.material.navigation.NavigationView
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity.activityDrawer
import timber.log.Timber
import javax.inject.Inject

@LogLifecykle
class Activity : DaggerAppCompatActivity(), AuthCaster.AuthCasterListener {
    @Inject
    lateinit var logoutHandler: LogoutHandler
    @Inject
    lateinit var authCaster: AuthCaster
    @Inject
    lateinit var authProvider: AuthProvider
    @Inject
    lateinit var sharedPref: SharedPref

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity)

        setSupportActionBar(findViewById(R.id.appBarToolbar))
        setupNavController()
        loadGraph()

        authCaster.subscribeToAuthError(this)
    }

    override fun onDestroy() {
        authCaster.unsubscribeFromAuthError(this)
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (activityDrawer.isDrawerOpen(GravityCompat.START)) {
            activityDrawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun loadGraph() {
        (supportFragmentManager.findFragmentById(R.id.activityNavigation) as? NavHostFragment)
            ?.navController
            ?.apply {
                graph = navInflater.inflate(R.navigation.nav_graph).also {
                    it.startDestination = chooseStartingDestination()
                }
            }
    }

    private fun setupNavController() {
        val nav = findNavController(R.id.activityNavigation)
        findViewById<NavigationView>(R.id.activityNavigationView).setupWithNavController(nav)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.animeFragment,
                R.id.mangaFragment,
                R.id.timelineFragment,
                R.id.profileFragment
            ),
            findViewById(R.id.activityDrawer)
        )

        setupActionBarWithNavController(nav, appBarConfiguration)
    }

    @IdRes
    private fun chooseStartingDestination(): Int {
        return if (authProvider.accessToken.isEmpty()) {
            if (sharedPref.isAnalyticsComplete) {
                R.id.detailsFragment
            } else {
                R.id.analyticsFragment
            }
        } else {
            R.id.animeFragment
        }
    }

    override fun onSupportNavigateUp() =
        findNavController(R.id.activityNavigation).navigateUp(appBarConfiguration)

    override fun unableToRefresh() {
        Timber.w("unableToRefresh has occurred")
        logout()
    }

    /**
     * Logs the user out and returns the user back to entering the login details.
     */
    fun logout() {
        Timber.w("Starting log out from Activity")
        // Maybe should move this into a coroutine
        with(HandlerThread("LogoutThread")) {
            start()
            Handler(looper).post {
                logoutHandler.executeLogout()
                quitSafely()
            }
        }

        findNavController(R.id.activityNavigation).navigate(
            OverviewNavGraphDirections.globalToDetailsFragment()
        )
    }
}
