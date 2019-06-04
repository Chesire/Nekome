package com.chesire.malime.flow.oob

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chesire.malime.SharedPref
import javax.inject.Inject

class AnalyticsViewModel @Inject constructor(private val sharedPref: SharedPref) : ViewModel() {
    val analyticsState = MutableLiveData<Boolean>(false)

    fun saveAnalyticsChoice() {
        sharedPref.analyticsEnabled = analyticsState.value ?: false
        sharedPref.analyticsComplete = true
    }
}
