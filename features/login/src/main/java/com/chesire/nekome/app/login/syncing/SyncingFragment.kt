package com.chesire.nekome.app.login.syncing

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.chesire.nekome.app.login.syncing.ui.SyncingScreen
import com.chesire.nekome.core.compose.theme.NekomeTheme
import com.chesire.nekome.core.nav.Flow
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment to show a user that their series are currently being synced down.
 */
@AndroidEntryPoint
class SyncingFragment : Fragment() {

    private lateinit var flow: Flow

    override fun onAttach(context: Context) {
        super.onAttach(context)
        flow = requireNotNull(context as? Flow) {
            "Hosting activity must implement ${Flow::class.java}"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            NekomeTheme {
                SyncingScreen(
                    finishAction = {
                        flow.finishLogin()
                    }
                )
            }
        }
    }
}
