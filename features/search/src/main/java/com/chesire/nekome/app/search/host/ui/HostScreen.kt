package com.chesire.nekome.app.search.host.ui

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HostScreen(
    viewModel: HostViewModel = viewModel()
) {
    val state = viewModel.uiState.collectAsState()
}

@Composable
private fun Render(state: State<UIState>) {
    Scaffold { paddingValues ->

    }
}
