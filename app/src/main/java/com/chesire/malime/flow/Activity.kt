package com.chesire.malime.flow

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.malime.AuthCaster
import com.chesire.malime.OverviewNavGraphDirections
import com.chesire.malime.R
import com.google.android.material.navigation.NavigationView
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity.activityDrawer
import timber.log.Timber
import javax.inject.Inject

@LogLifecykle
class Activity : DaggerAppCompatActivity(), AuthCaster.AuthCasterListener {
    @Inject
    lateinit var authCaster: AuthCaster
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProviders
            .of(this, viewModelFactory)
            .get(ActivityViewModel::class.java)
    }

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
                    it.startDestination = viewModel.startingFragment
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

    override fun onSupportNavigateUp() =
        findNavController(R.id.activityNavigation).navigateUp(appBarConfiguration)

    override fun unableToRefresh() {
        Timber.w("unableToRefresh has occurred")
        logout()
    }

    fun logout() {
        Timber.w("Logout called, now attempting")
        viewModel.logout {
            Handler(Looper.getMainLooper()).post {
                findNavController(R.id.activityNavigation).navigate(
                    OverviewNavGraphDirections.globalToDetailsFragment()
                )
            }
        }
    }
}
