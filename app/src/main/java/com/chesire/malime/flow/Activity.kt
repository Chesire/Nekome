package com.chesire.malime.flow

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import androidx.annotation.IdRes
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.malime.AuthCaster
import com.chesire.malime.LogoutHandler
import com.chesire.malime.OverviewNavGraphDirections
import com.chesire.malime.R
import com.chesire.malime.SharedPref
import com.chesire.malime.extensions.hide
import com.chesire.malime.extensions.show
import com.chesire.malime.kitsu.AuthProvider
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity.activityBottomNavigation
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

    private fun loadGraph() {
        val host =
            supportFragmentManager.findFragmentById(R.id.activityNavigation) as NavHostFragment
        val controller = host.navController
        val graph = controller.navInflater.inflate(R.navigation.nav_graph)

        graph.startDestination = chooseStartingDestination()

        controller.graph = graph
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
        setupWithNavController(activityBottomNavigation, this)
        addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.animeFragment -> activityBottomNavigation.show()
                R.id.mangaFragment -> activityBottomNavigation.show()
                R.id.profileFragment -> activityBottomNavigation.show()
                R.id.timelineFragment -> activityBottomNavigation.show()
                else -> activityBottomNavigation.hide()
            }
        }
    }

    override fun unableToRefresh() {
        Timber.w("unableToRefresh has occurred")

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
