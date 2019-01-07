package com.chesire.malime.view.preferences

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.chesire.malime.R

class PrefFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName =
                requireContext().getString(R.string.key_shared_pref_file_name)
        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //findPreference(getString(R.string.key_view_licenses)).setOnPreferenceClickListener {
        //    startActivity(Intent(requireActivity(), OssLicensesMenuActivity::class.java))
        //    true
        //}
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
