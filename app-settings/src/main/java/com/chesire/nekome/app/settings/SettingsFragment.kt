package com.chesire.nekome.app.settings

import android.net.Uri
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsIntent
import androidx.navigation.fragment.findNavController
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.nekome.core.flags.UserSeriesStatus
import timber.log.Timber

/**
 * [SettingsFragment] hosts the configuration options for the application.
 */
@LogLifecykle
class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var keyPrivacyPolicy: String
    private lateinit var keyLicenses: String
    private lateinit var keyGithub: String
    private lateinit var keyDefaultSeriesState: String

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        keyPrivacyPolicy = getString(R.string.key_privacy_policy)
        keyLicenses = getString(R.string.key_licenses)
        keyGithub = getString(R.string.key_github)
        keyDefaultSeriesState = getString(R.string.key_default_series_state)

        findPreference<ListPreference>(keyDefaultSeriesState)?.let { pref ->
            val userSeriesMap = UserSeriesStatus.getValueMap(requireContext())
            pref.entries = userSeriesMap.values.toTypedArray()
            pref.entryValues = userSeriesMap.keys.map { it.toString() }.toTypedArray()
        }
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        when (preference?.key) {
            keyPrivacyPolicy -> showPrivacyPolicy()
            keyGithub -> showGithub()
            keyLicenses -> showLicenses()
            else -> {
                Timber.w("Unexpected key for onPreferenceClick - ${preference?.key}")
                return super.onPreferenceTreeClick(preference)
            }
        }

        return true
    }

    private fun showPrivacyPolicy() {
        CustomTabsIntent.Builder()
            .build()
            .launchUrl(requireContext(), Uri.parse(getString(R.string.privacy_policy_url)))
    }

    private fun showGithub() {
        CustomTabsIntent.Builder()
            .build()
            .launchUrl(requireContext(), Uri.parse(getString(R.string.settings_github_url)))
    }

    private fun showLicenses() =
        findNavController().navigate(SettingsFragmentDirections.toOssFragment())
}
