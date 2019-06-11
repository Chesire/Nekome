package com.chesire.malime.flow.settings

import android.content.Context
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.malime.R
import com.chesire.malime.flow.Activity
import timber.log.Timber

@LogLifecykle
class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var keyLogOut: String
    private lateinit var keyPrivacyPolicy: String
    private lateinit var keyOss: String

    // Use the activity to force log out
    private lateinit var parentActivity: Activity

    override fun onAttach(context: Context) {
        super.onAttach(context)

        parentActivity = checkNotNull(activity as? Activity) {
            "Activity hosting SettingsFragment was not Activity class"
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        keyLogOut = getString(R.string.key_log_out)
        keyPrivacyPolicy = getString(R.string.key_privacy_policy)
        keyOss = getString(R.string.key_oss)
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        when (preference?.key) {
            keyLogOut -> parentActivity.logout()
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
