package com.chesire.malime.flow.overview.manga

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.chesire.malime.databinding.FragmentMangaBinding
import dagger.android.support.DaggerFragment

class MangaFragment : DaggerFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentMangaBinding.inflate(inflater, container, false).root

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
