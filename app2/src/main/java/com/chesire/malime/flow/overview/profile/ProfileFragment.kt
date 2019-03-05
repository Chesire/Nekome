package com.chesire.malime.flow.overview.profile

import dagger.android.support.DaggerFragment

class ProfileFragment : DaggerFragment() {

    companion object {
        const val TAG = "ProfileFragment"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ProfileFragment.
         */
        fun newInstance() = ProfileFragment()
    }
}
