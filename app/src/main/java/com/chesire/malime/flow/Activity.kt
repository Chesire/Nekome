package com.chesire.malime.flow

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
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
        ViewModelProvider(this, viewModelFactory).get<ActivityViewModel>()
    }

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity)
        setSupportActionBar(findViewById(R.id.appBarToolbar))
        setupNavController()
        observeViewModel()

        authCaster.subscribeToAuthError(this)

        if (!viewModel.userLoggedIn) {
            findNavController(R.id.activityNavigation)
                .navigate(OverviewNavGraphDirections.globalToDetailsFragment())
        }
    }

    private fun observeViewModel() {
        viewModel.user.observe(this, Observer { userModel ->
            if (userModel == null) {
                return@Observer
            }

            findViewById<NavigationView>(R.id.activityNavigationView)?.getHeaderView(0)?.let {
                it.findViewById<TextView>(R.id.viewNavHeaderTitle).text = userModel.name
                it.findViewById<TextView>(R.id.viewNavHeaderSubtitle).text = userModel.service.name

                Glide.with(this)
                    .load(userModel.avatar.medium.url)
                    .optionalCircleCrop()
                    .into(it.findViewById(R.id.viewNavHeaderImage))
            }
        })
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
        nav.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.detailsFragment, R.id.syncingFragment -> disableDrawer()
                else -> enableDrawer()
            }
        }
    }

    override fun onSupportNavigateUp() =
        findNavController(R.id.activityNavigation).navigateUp(appBarConfiguration)

    override fun unableToRefresh() {
        Timber.w("unableToRefresh has occurred")
        logout()
    }

    /**
     * Sends a logout request to the [viewModel] to handle.
     */
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

    private fun disableDrawer() {
        supportActionBar?.hide()
        activityDrawer.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)
    }

    private fun enableDrawer() {
        supportActionBar?.show()
        activityDrawer.setDrawerLockMode(LOCK_MODE_UNLOCKED)
    }
}
