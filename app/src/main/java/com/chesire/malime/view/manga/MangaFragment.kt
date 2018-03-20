package com.chesire.malime.view.manga

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chesire.malime.R

class MangaFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maldisplay, container, false)
    }

    companion object {
        const val tag = "MangaFragment"
        fun newInstance(): MangaFragment {
            val mangaFragment = MangaFragment()
            val args = Bundle()
            mangaFragment.arguments = args
            return mangaFragment
        }
    }
}