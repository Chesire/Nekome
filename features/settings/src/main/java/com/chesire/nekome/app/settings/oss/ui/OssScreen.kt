package com.chesire.nekome.app.settings.oss.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mikepenz.aboutlibraries.ui.compose.LibrariesContainer

@Composable
fun OssScreen() {
    Scaffold { paddingValues ->
        LibrariesContainer(
            Modifier
                .padding(paddingValues)
                .fillMaxSize()
        )
    }
}
