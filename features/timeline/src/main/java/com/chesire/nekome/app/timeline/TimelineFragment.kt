package com.chesire.nekome.app.timeline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.chesire.lifecyklelog.LogLifecykle
import com.chesire.nekome.app.timeline.databinding.FragmentTimelineBinding

/**
 * Fragment to display the users timeline.
 */
@LogLifecykle
class TimelineFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentTimelineBinding.inflate(inflater, container, false).root
}
