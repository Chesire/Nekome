package com.chesire.nekome.app.series.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.chesire.nekome.app.series.item.ui.ItemScreen
import com.chesire.nekome.core.compose.theme.NekomeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItemFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            NekomeTheme {
                ItemScreen()
            }
        }
    }
}
