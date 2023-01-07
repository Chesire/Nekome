package com.chesire.nekome.app.search.results

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.chesire.nekome.app.search.results.ui.ResultsScreen
import com.chesire.nekome.core.compose.theme.NekomeTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Displays the results of a search to the user, allowing them to select to track new series.
 */
@AndroidEntryPoint
class ResultsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            NekomeTheme {
                ResultsScreen()
            }
        }
    }
}
