package com.chesire.malime.flow.overview.settings

import dagger.android.support.DaggerFragment

class SettingsFragment : DaggerFragment() {

    companion object {
        const val TAG = "SettingsFragment"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment SettingsFragment.
         */
        fun newInstance() = SettingsFragment()
    }
}
