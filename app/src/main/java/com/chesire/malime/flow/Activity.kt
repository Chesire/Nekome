package com.chesire.malime.flow

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity)

        loadGraph()
        setupNavController()

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

    private fun setupNavController() = with(findNavController(R.id.activityNavigation)) {
        findViewById<NavigationView>(R.id.activityNavigationView).setupWithNavController(this)
    }

    override fun unableToRefresh() {
        Timber.w("unableToRefresh has occurred")
        logout()
    }

    /**
     * Sets the navigation drawer icon on the provided [toolbar].
     */
    fun setNavigationDrawerIcon(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        val toggle = ActionBarDrawerToggle(
            this,
            this.findViewById(R.id.activityDrawer),
            toolbar,
            0,
            0
        )
        toggle.isDrawerIndicatorEnabled = true;
        toggle.syncState();
    }

    /**
     * Logs the user out and returns the user back to entering the login details.
     */
    fun logout() {
        Timber.w("Starting log out from Activity")
        val handlerThread = HandlerThread("LogoutThread")
        handlerThread.start()
        Handler(handlerThread.looper).post {
            logoutHandler.executeLogout()
            findNavController(R.id.activityNavigation).navigate(
                OverviewNavGraphDirections.globalToDetailsFragment()
            )
            handlerThread.quitSafely()
        }
    }
}
