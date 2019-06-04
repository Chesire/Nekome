package com.chesire.malime.flow.login

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.malime.R
import com.chesire.malime.SharedPref
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

@LogLifecykle
class LoginActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val host =
            supportFragmentManager.findFragmentById(R.id.activityLoginNavigation) as NavHostFragment
        val controller = host.navController
        val graph = controller.navInflater.inflate(R.navigation.nav_graph_login)

        if (sharedPref.analyticsComplete) {
            graph.startDestination = R.id.detailsFragment
        } else {
            graph.startDestination = R.id.analyticsFragment
        }

        controller.graph = graph
    }
}
