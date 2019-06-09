package com.chesire.malime.flow.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.malime.R

@LogLifecykle
class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}
