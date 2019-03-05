package com.chesire.malime.flow.overview.anime

import dagger.android.support.DaggerFragment

class AnimeFragment : DaggerFragment() {

    companion object {
        const val TAG = "AnimeFragment"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment AnimeFragment.
         */
        fun newInstance() = AnimeFragment()
    }
}
