package com.chesire.malime.flow.overview.manga

import dagger.android.support.DaggerFragment

class MangaFragment : DaggerFragment() {

    companion object {
        const val TAG = "MangaFragment"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment MangaFragment.
         */
        fun newInstance() = MangaFragment()
    }
}
