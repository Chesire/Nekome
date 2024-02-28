package com.chesire.nekome.app.login.syncing.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.chesire.nekome.app.login.R
import com.chesire.nekome.resources.StringResource

@Composable
fun SyncingScreen(
    viewModel: SyncingViewModel = hiltViewModel(),
    finishAction: () -> Unit
) {
    val state = viewModel.uiState.collectAsState()

    val finishedSyncing = state.value.finishedSyncing
    if (finishedSyncing == true) {
        LaunchedEffect(finishedSyncing) {
            finishAction()
            viewModel.observeFinishedSyncing()
        }
    }

    Render(state)
}

@Composable
private fun Render(state: State<UIState>) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .semantics { testTag = SyncingTags.Root },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Avatar(
                imageUrl = state.value.avatar,
                modifier = Modifier.padding(16.dp, 32.dp, 16.dp, 16.dp)
            )
            SyncingText(modifier = Modifier.padding(16.dp))
            SyncingProgress()
        }
    }
}

@Composable
private fun Avatar(imageUrl: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        placeholder = painterResource(id = R.drawable.ic_account_circle),
        modifier = modifier
            .clip(CircleShape)
            .size(120.dp, 120.dp)
    )
}

@Composable
private fun SyncingText(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(id = StringResource.syncing_syncing),
        fontSize = 24.sp,
        modifier = modifier
    )
}

@Composable
private fun SyncingProgress(modifier: Modifier = Modifier) {
    LinearProgressIndicator(
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier
    )
}
//
//@Composable
//@Preview
//private fun Preview() {
//    val initialState = UIState(
//        avatar = "https://media.kitsu.io/users/avatars/294558/large.jpeg",
//        finishedSyncing = null
//    )
//    NekomeTheme() {
//        Render(
//            state = produceState(initialValue = initialState, producer = { value = initialState })
//        )
//    }
//}

object SyncingTags {
    const val Root = "SyncingRoot"
}
