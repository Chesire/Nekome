package com.chesire.nekome.app.timeline

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.chesire.nekome.app.timeline.databinding.FragmentTimelineBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment to display the users timeline.
 */
@AndroidEntryPoint
class TimelineFragment : Fragment(R.layout.fragment_timeline) {

    private var _binding: FragmentTimelineBinding? = null
    private val binding get() = requireNotNull(_binding) { "Binding not set" }
    private val viewModel by viewModels<TimelineViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.latestActivity.observe(viewLifecycleOwner) {
            val result = it
            Log.d("Nekome", result.toString())
        }
    }
}
