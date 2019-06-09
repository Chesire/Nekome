package com.chesire.malime.flow.settings

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.malime.R
import timber.log.Timber

@LogLifecykle
class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var keyLogOut: String
    private lateinit var keyPrivacyPolicy: String
    private lateinit var keyOss: String

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        keyLogOut = requireContext().getString(R.string.key_log_out)
        keyPrivacyPolicy = requireContext().getString(R.string.key_privacy_policy)
        keyOss = requireContext().getString(R.string.key_oss)
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        when (preference?.key) {
            keyLogOut -> {
                Timber.d("Beginning log out")
                // perform logout logic
            }
            keyPrivacyPolicy -> {
                // load url
            }
            keyOss -> {
                // load oss fragment
            }
            else -> {
                Timber.w("Unexpected key for onPreferenceClick - ${preference?.key}")
                return super.onPreferenceTreeClick(preference)
            }
        }

        return true
    }
}
