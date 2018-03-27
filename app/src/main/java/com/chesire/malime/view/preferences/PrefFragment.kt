package com.chesire.malime.view.preferences

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import com.chesire.malime.R

class PrefFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
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