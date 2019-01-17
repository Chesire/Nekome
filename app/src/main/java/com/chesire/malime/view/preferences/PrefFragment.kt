package com.chesire.malime.view.preferences

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.chesire.malime.R
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.LibsBuilder

class PrefFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName =
                requireContext().getString(R.string.key_shared_pref_file_name)
        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findPreference(getString(R.string.key_about_app)).setOnPreferenceClickListener {
            LibsBuilder()
                .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                .withActivityTitle(getString(R.string.aboutLibraries_description_title))
                .start(requireContext())
            true
        }
    }

    companion object {
        const val tag = "PrefFragment"
        fun newInstance() = PrefFragment().apply { arguments = Bundle() }
    }
}
