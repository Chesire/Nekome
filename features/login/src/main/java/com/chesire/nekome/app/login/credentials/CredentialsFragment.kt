package com.chesire.nekome.app.login.credentials

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.chesire.core.compose.theme.NekomeTheme
import com.chesire.nekome.app.login.credentials.ui.CredentialsScreen
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
                CredentialsScreen()
            }
        }
    }
}
