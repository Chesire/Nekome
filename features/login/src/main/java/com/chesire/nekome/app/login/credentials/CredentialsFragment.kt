package com.chesire.nekome.app.login.credentials

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.chesire.nekome.app.login.credentials.ui.CredentialsScreen
import com.chesire.nekome.app.login.credentials.ui.CredentialsViewModel
import com.chesire.nekome.app.login.credentials.ui.ViewAction
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CredentialsFragment : Fragment() {

    private val viewModel by viewModels<CredentialsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            CredentialsScreen(
                state = viewModel.viewState.collectAsState(),
                onUsernameChanged = { viewModel.execute(ViewAction.UsernameChanged(it)) },
                onPasswordChanged = { viewModel.execute(ViewAction.PasswordChanged(it)) },
                onForgotPasswordPressed = { viewModel.execute(ViewAction.ForgotPasswordPressed) },
                onLoginPressed = { viewModel.execute(ViewAction.LoginPressed) },
                onSignupPressed = { viewModel.execute(ViewAction.SignUpPressed) }
            )
        }
    }
}
