package com.chesire.malime.flow.settings

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsIntent
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.malime.R
import com.chesire.malime.flow.Activity
import timber.log.Timber

@LogLifecykle
class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var keyAnalytics: String
    private lateinit var keyLogOut: String
    private lateinit var keyPrivacyPolicy: String
    private lateinit var keyLicenses: String

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
        keyAnalytics = getString(R.string.preference_analytics_enabled)
        keyLogOut = getString(R.string.key_log_out)
        keyPrivacyPolicy = getString(R.string.key_privacy_policy)
        keyLicenses = getString(R.string.key_licenses)
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        when (preference?.key) {
            keyAnalytics -> handleAnalyticsToggled()
            keyLogOut -> showLogoutDialog()
            keyPrivacyPolicy -> showPrivacyPolicy()
            keyLicenses -> showLicenses()
            else -> {
                Timber.w("Unexpected key for onPreferenceClick - ${preference?.key}")
                return super.onPreferenceTreeClick(preference)
            }
        }

        return true
    }

    private fun handleAnalyticsToggled() {
        // Change the analytics enabled state
    }

    private fun showLogoutDialog() {
        MaterialDialog(requireContext()).show {
            message(R.string.settings_logout_prompt_message)
            positiveButton(R.string.settings_logout_prompt_confirm) {
                parentActivity.logout()
            }
            negativeButton(R.string.settings_logout_prompt_cancel)
            lifecycleOwner(viewLifecycleOwner)
        }
    }

    private fun showPrivacyPolicy() {
        CustomTabsIntent.Builder()
            .build()
            .launchUrl(requireContext(), Uri.parse(getString(R.string.privacy_policy_url)))
    }

    private fun showLicenses() {
        // Load the licenses fragment
    }
}
