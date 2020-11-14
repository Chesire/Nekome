package com.chesire.nekome

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.nekome.core.AuthCaster
import com.chesire.nekome.core.flags.HomeScreenOptions
import com.chesire.nekome.core.nav.Flow
import com.chesire.nekome.core.settings.ApplicationSettings
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity.activityDrawer
import timber.log.Timber
import javax.inject.Inject

/**
 * Single host activity for the application, handles all required logic of the activity such as
 * hiding/showing the drawer.
 */
@LogLifecykle
@AndroidEntryPoint
@Suppress("TooManyFunctions")
class Activity : AppCompatActivity(), AuthCaster.AuthCasterListener, Flow {
    @Inject
    lateinit var authCaster: AuthCaster

    @Inject
    lateinit var settings: ApplicationSettings

    private val viewModel by viewModels<ActivityViewModel>()

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity)
        setSupportActionBar(findViewById(R.id.appBarToolbar))
        setupNavController()
        observeViewModel()

        authCaster.subscribeToAuthError(this)

        // savedInstanceState check is needed or the DetailsFragment will be recreated
        // on configuration change
        if (savedInstanceState == null && !viewModel.userLoggedIn) {
            findNavController(R.id.activityNavigation)
                .navigate(OverviewNavGraphDirections.globalToDetailsFragment())
        }
    }

    private fun observeViewModel() {
        viewModel.user.observe(this) { userModel ->
            if (userModel != null) {
                updateAvatar(findViewById(R.id.activityNavigationView), userModel)
            }
        }
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
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.animeFragment,
                R.id.mangaFragment,
                R.id.discoverFragment,
                R.id.searchFragment
            ),
            findViewById<DrawerLayout>(R.id.activityDrawer)
        )

        with(findNavController(R.id.activityNavigation)) {
            findViewById<NavigationView>(R.id.activityNavigationView).setupWithNavController(this)
            setupActionBarWithNavController(this, appBarConfiguration)
            addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.detailsFragment, R.id.syncingFragment -> disableDrawer()
                    else -> enableDrawer()
                }
            }
        }

        navigateToDefaultHome()
    }

    override fun onSupportNavigateUp() =
        findNavController(R.id.activityNavigation).navigateUp(appBarConfiguration)

    override fun unableToRefresh() {
        Timber.w("unableToRefresh has occurred")
        performLogout(true)
    }

    /**
     * Shows a dialog asking the user if they wish to logout.
     * This is called from the menu_navigation.xml menu resource in order to allow it to still
     * function while using the nav component.
     */
    fun requestUserLogout(@Suppress("UNUSED_PARAMETER") item: MenuItem) {
        MaterialDialog(this).show {
            message(R.string.menu_logout_prompt_message)
            positiveButton(R.string.menu_logout_prompt_confirm) {
                performLogout()
            }
            negativeButton(R.string.menu_logout_prompt_cancel)
            lifecycleOwner(this@Activity)
        }
    }

    private fun performLogout(isFailure: Boolean = false) {
        Timber.w("Logout called, now attempting")
        viewModel.logout {
            Handler(Looper.getMainLooper()).post {
                findNavController(R.id.activityNavigation).navigate(
                    OverviewNavGraphDirections.globalToDetailsFragment()
                )

                if (isFailure) {
                    findViewById<View?>(R.id.activityDrawer)?.let { view ->
                        Snackbar.make(view, R.string.logout_forced, Snackbar.LENGTH_LONG).show()
                    }
                }
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

    override fun finishLogin() {
        navigateToDefaultHome()
    }

    private fun navigateToDefaultHome() {
        if (settings.defaultHomeScreen == HomeScreenOptions.Anime) {
            findNavController(R.id.activityNavigation).navigate(
                OverviewNavGraphDirections.globalToAnimeFragment()
            )
        } else {
            findNavController(R.id.activityNavigation).navigate(
                OverviewNavGraphDirections.globalToMangaFragment()
            )
        }
    }
}
