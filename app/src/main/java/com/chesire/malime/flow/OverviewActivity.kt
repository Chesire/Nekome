package com.chesire.malime.flow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.chesire.malime.R
import kotlinx.android.synthetic.main.activity_overview.activityOverviewBottomNavigation

class OverviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)

        setupWithNavController(
            activityOverviewBottomNavigation,
            findNavController(R.id.activityOverviewNavigation)
        )
    }
}
