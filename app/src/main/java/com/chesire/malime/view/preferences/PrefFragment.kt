package com.chesire.malime.view.preferences

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import com.chesire.malime.R
import com.chesire.malime.util.SharedPref

class PrefFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        val manager = preferenceManager
        manager.sharedPreferencesName = SharedPref(context!!).sharedPrefFile
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