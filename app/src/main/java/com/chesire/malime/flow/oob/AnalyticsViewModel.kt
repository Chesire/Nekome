package com.chesire.malime.flow.oob

import androidx.lifecycle.ViewModel
import com.chesire.malime.SharedPref
import javax.inject.Inject

class AnalyticsViewModel @Inject constructor(private val sharedPref: SharedPref) : ViewModel() {
    fun saveAnalyticsChoice(enable: Boolean) {
        sharedPref.isAnalyticsEnabled = enable
        sharedPref.isAnalyticsComplete = true
    }
}
