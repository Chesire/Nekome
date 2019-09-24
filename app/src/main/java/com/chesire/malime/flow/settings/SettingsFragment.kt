package com.chesire.malime.flow.settings

import android.net.Uri
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsIntent
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.malime.R
import timber.log.Timber

@LogLifecykle
class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var keyPrivacyPolicy: String
    private lateinit var keyLicenses: String

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        keyPrivacyPolicy = getString(R.string.key_privacy_policy)
        keyLicenses = getString(R.string.key_licenses)
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        when (preference?.key) {
            keyPrivacyPolicy -> showPrivacyPolicy()
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

    private fun showLicenses() =
        findNavController().navigate(SettingsFragmentDirections.toOssFragment())
}
