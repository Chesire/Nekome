package com.chesire.nekome.app.login.credentials

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.chesire.nekome.app.login.credentials.ui.CredentialsScreen
import com.chesire.nekome.core.compose.theme.NekomeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CredentialsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            NekomeTheme {
                CredentialsScreen(
                    finishAction = {
                        findNavController().navigate(
                            CredentialsFragmentDirections.toSyncingFragment()
                        )
                    }
                )
            }
        }
    }
}
