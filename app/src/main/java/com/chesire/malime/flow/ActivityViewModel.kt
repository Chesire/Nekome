package com.chesire.malime.flow

import androidx.annotation.IdRes
import androidx.lifecycle.ViewModel
import com.chesire.malime.R
import com.chesire.malime.SharedPref
import com.chesire.malime.kitsu.AuthProvider
import javax.inject.Inject

/**
 * [ViewModel] scoped to the [Activity].
 */
class ActivityViewModel @Inject constructor(
    private val authProvider: AuthProvider,
    private val sharedPref: SharedPref
) : ViewModel() {

    /**
     * Checks against stored data to decide which fragment should be displayed on start.
     */
    val startingFragment: Int
        @IdRes
        get() {
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
}
