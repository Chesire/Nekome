package com.chesire.malime.flow.overview.activity

import dagger.android.support.DaggerFragment

class ActivityFragment : DaggerFragment() {

    companion object {
        const val TAG = "ActivityFragment"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ActivityFragment.
         */
        fun newInstance() = ActivityFragment()
    }
}
