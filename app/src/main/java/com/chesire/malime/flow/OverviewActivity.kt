package com.chesire.malime.flow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.chesire.malime.AuthCaster
import com.chesire.malime.R
import com.chesire.malime.extensions.hide
import com.chesire.malime.extensions.show
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_overview.activityOverviewBottomNavigation
import javax.inject.Inject

class OverviewActivity : DaggerAppCompatActivity(), AuthCaster.AuthCasterListener {
    @Inject
    lateinit var authCaster: AuthCaster

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)
        setupNavController()
        authCaster.subscribeToAuthError(this)
    }

    override fun onDestroy() {
        authCaster.unsubscribeFromAuthError(this)
        super.onDestroy()
    }

    private fun setupNavController() = with(findNavController(R.id.activityOverviewNavigation)) {
        setupWithNavController(activityOverviewBottomNavigation, this)
        addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.animeFragment -> activityOverviewBottomNavigation.show()
                R.id.mangaFragment -> activityOverviewBottomNavigation.show()
                R.id.profileFragment -> activityOverviewBottomNavigation.show()
                R.id.activityFragment -> activityOverviewBottomNavigation.show()
                R.id.settingsFragment -> activityOverviewBottomNavigation.show()
                else -> activityOverviewBottomNavigation.hide()
            }
        }
    }

    override fun unableToRefresh() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
