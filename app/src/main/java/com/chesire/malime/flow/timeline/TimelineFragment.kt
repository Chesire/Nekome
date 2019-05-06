package com.chesire.malime.flow.timeline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.malime.databinding.FragmentTimelineBinding
import dagger.android.support.DaggerFragment

@LogLifecykle
class TimelineFragment : DaggerFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentTimelineBinding.inflate(inflater, container, false).root
}
