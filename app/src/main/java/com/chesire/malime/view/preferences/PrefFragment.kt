package com.chesire.malime.view.preferences

import android.content.Intent
import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import com.chesire.malime.R
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity

class PrefFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName =
                requireContext().getString(R.string.key_shared_pref_file_name)
        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findPreference(getString(R.string.key_view_licenses)).setOnPreferenceClickListener {
            startActivity(Intent(requireActivity(), OssLicensesMenuActivity::class.java))
            true
        }
    }

    companion object {
        const val tag = "PrefFragment"
        fun newInstance(): PrefFragment {
            val prefFragment = PrefFragment()
            val args = Bundle()
            prefFragment.arguments = args
            return prefFragment
        }
    }
}
