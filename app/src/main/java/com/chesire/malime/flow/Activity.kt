package com.chesire.malime.flow

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import androidx.annotation.IdRes
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
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


    /*


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_tools -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    // NavigationView.OnNavigationItemSelectedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
    }
     */
}
