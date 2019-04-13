package com.chesire.malime.flow.series.list.manga

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.malime.databinding.FragmentMangaBinding
import dagger.android.support.DaggerFragment

@LogLifecykle
class MangaFragment : DaggerFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentMangaBinding.inflate(inflater, container, false).root
}
