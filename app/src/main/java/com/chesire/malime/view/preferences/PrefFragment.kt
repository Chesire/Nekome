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

    companion object {
        const val tag = "PrefFragment"
        fun newInstance() = PrefFragment().apply { arguments = Bundle() }
    }
}
