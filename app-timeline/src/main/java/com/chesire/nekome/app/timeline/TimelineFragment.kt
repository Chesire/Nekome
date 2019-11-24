package com.chesire.nekome.app.timeline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chesire.lifecyklelog.LogLifecykle
import dagger.android.support.DaggerFragment

@LogLifecykle
class TimelineFragment : DaggerFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_timeline, container, false)
}
