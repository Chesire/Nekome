package com.chesire.malime.flow.overview.anime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.chesire.malime.databinding.FragmentAnimeBinding
import dagger.android.support.DaggerFragment

class AnimeFragment : DaggerFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentAnimeBinding.inflate(inflater, container, false).root

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
