package com.chesire.nekome.app.search.host.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chesire.nekome.app.search.R
import com.chesire.nekome.app.search.host.core.model.SearchGroup
import com.chesire.nekome.core.compose.theme.NekomeTheme

@Composable
fun HostScreen(
    viewModel: HostViewModel = viewModel()
) {
    val state = viewModel.uiState.collectAsState()

    Render(
        state = state,
        onInputTextChanged = { viewModel.execute(ViewAction.SearchTextUpdated(it)) },
        onSearchPressed = { viewModel.execute(ViewAction.ExecuteSearch) },
    )
}

@Composable
private fun Render(
    state: State<UIState>,
    onInputTextChanged: (String) -> Unit,
    onSearchPressed: () -> Unit
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .semantics {
                    testTag = HostTags.Root
                }
        ) {
            InputText(state.value.searchText, onInputTextChanged)
            SearchGroup(state.value.searchGroup)
            SearchButton(onSearchPressed)
        }
    }
}

@Composable
private fun InputText(text: String, onInputTextChanged: (String) -> Unit) {
    OutlinedTextField(
        value = text,
        onValueChange = onInputTextChanged,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            autoCorrect = false
            // imeAction = ImeAction.Next
        ),
        // keyboardActions = KeyboardActions(
        //     onNext = { focusManager.moveFocus(FocusDirection.Down) },
        // ),
        // isError = isUsernameError,
        singleLine = true,
        label = { Text(text = stringResource(id = R.string.search_series_title)) },
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                testTag = HostTags.Input
            }
    )
}

@Composable
private fun SearchGroup(group: SearchGroup) {

}

@Composable
private fun SearchButton(onSearchPressed: () -> Unit) {

}

@Composable
@Preview
private fun Preview() {
    val initialState = UIState(
        searchText = "Some initial search text",
        searchGroup = SearchGroup.Anime
    )
    NekomeTheme(darkTheme = true) {
        Render(
            state = produceState(initialValue = initialState, producer = { value = initialState }),
            onInputTextChanged = { /**/ },
            onSearchPressed = { /**/ }
        )
    }
}

object HostTags {
    const val Root = "HostRoot"
    const val Input = "HostInput"
}
