package com.chesire.malime.app.discover.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.malime.app.discover.R
import com.chesire.malime.core.viewmodel.ViewModelFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

@LogLifecykle
class SearchFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_search, container, false)
}
